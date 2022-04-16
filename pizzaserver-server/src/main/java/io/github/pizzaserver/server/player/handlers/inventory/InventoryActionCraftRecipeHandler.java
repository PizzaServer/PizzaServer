package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.event.type.player.PlayerCraftEvent;
import io.github.pizzaserver.api.inventory.CraftingInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.type.ShapedRecipe;
import io.github.pizzaserver.api.recipe.type.ShapelessRecipe;
import io.github.pizzaserver.server.inventory.ImplPlayerCraftingInventory;
import io.github.pizzaserver.server.network.data.inventory.actions.CraftRecipeRequestActionDataWrapper;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.Optional;

public class InventoryActionCraftRecipeHandler extends InventoryActionHandler<CraftRecipeRequestActionDataWrapper> {

    public static final InventoryActionHandler<CraftRecipeRequestActionDataWrapper> INSTANCE = new InventoryActionCraftRecipeHandler();


    @Override
    protected boolean isValid(Player player, CraftRecipeRequestActionDataWrapper action) {
        boolean craftingResultSlotFree = ((ImplPlayerCraftingInventory) player.getInventory().getCraftingGrid()).getCreativeOutput().isEmpty();
        if (!craftingResultSlotFree || action.getRecipe().isEmpty()) {
            return false;
        }

        CraftingInventory craftingInventory = this.getCraftingInventory(player);

        Recipe recipe = action.getRecipe().get();

        if (!craftingInventory.getRecipeBlockType().equals(recipe.getBlockType())) {
            // We can only craft recipes if the crafting inventory block matches the recipe requirement.
            return false;
        }

        switch (recipe.getType()) {
            case SHAPELESS -> {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

                for (Item ingredient : shapelessRecipe.getIngredients()) {
                    if (!craftingInventory.contains(ingredient)) {
                        return false;
                    }
                }

                return true;
            }
            case SHAPED -> {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                return this.getShapedCraftingOffset(shapedRecipe, craftingInventory).isPresent();
            }
            case FURNACE -> {
                return false;
            }
            default -> {
                throw new NullPointerException("Unknown recipe type when trying to handle CRAFT_RECIPE: " + recipe.getType());
            }
        }
    }

    @Override
    protected boolean runAction(Player player, CraftRecipeRequestActionDataWrapper action) {
        CraftingInventory craftingInventory = this.getCraftingInventory(player);

        Recipe recipe = action.getRecipe().get();

        PlayerCraftEvent craftEvent = new PlayerCraftEvent(player, craftingInventory, recipe);
        player.getServer().getEventManager().call(craftEvent);
        if (craftEvent.isCancelled()) {
            return false;
        }

        switch (recipe.getType()) {
            case SHAPELESS -> {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

                for (Item ingredient : shapelessRecipe.getIngredients()) {
                    craftingInventory.removeItem(ingredient);
                }

                Item craftingOutput = shapelessRecipe.getOutput()[0];
                ((ImplPlayerCraftingInventory) player.getInventory().getCraftingGrid()).setCreativeOutput(craftingOutput);

                for (int i = 1; i < shapelessRecipe.getOutput().length; i++) {
                    Optional<Item> excess = player.getInventory().addItem(shapelessRecipe.getOutput()[i]);

                    // if it was unable to add all the output items, drop the output item.
                    excess.ifPresent(item -> ((ImplPlayer) player).tryDroppingItem(player.getInventory(), item));
                }
            }
            case SHAPED -> {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                ShapedRecipeOffsetData craftingGridOffset = this.getShapedCraftingOffset(shapedRecipe, craftingInventory).get();

                int xOffset = craftingGridOffset.getXOffset();
                int yOffset = craftingGridOffset.getYOffset();

                for (int x = 0; x < shapedRecipe.getGrid().getWidth(); x++) {
                    for (int y = 0; y < shapedRecipe.getGrid().getHeight(); y++) {
                        // Shaped recipes can be reversed horizontally.
                        Item requiredItem;
                        if (craftingGridOffset.isReversed()) {
                            requiredItem = shapedRecipe.getGrid().getItem(shapedRecipe.getGrid().getWidth() - x - 1, y);
                        } else {
                            requiredItem = shapedRecipe.getGrid().getItem(x, y);
                        }

                        int gridSlot = ((y + yOffset) * craftingInventory.getGridWidth()) + (x + xOffset);
                        Item itemInGrid = craftingInventory.getSlot(gridSlot);

                        itemInGrid.setCount(itemInGrid.getCount() - requiredItem.getCount());
                        craftingInventory.setSlot(gridSlot, itemInGrid);
                    }
                }

                Item craftingOutput = shapedRecipe.getGrid().getOutput()[0];
                ((ImplPlayerCraftingInventory) player.getInventory().getCraftingGrid()).setCreativeOutput(craftingOutput);

                for (int i = 1; i < shapedRecipe.getGrid().getOutput().length; i++) {
                    Optional<Item> excess = player.getInventory().addItem(shapedRecipe.getGrid().getOutput()[i]);

                    // if it was unable to add all the output items, drop the output item.
                    excess.ifPresent(item -> ((ImplPlayer) player).tryDroppingItem(player.getInventory(), item));
                }
            }
        }
        return true;
    }

    private Optional<ShapedRecipeOffsetData> getShapedCraftingOffset(ShapedRecipe shapedRecipe, CraftingInventory inventory) {
        // Ensure recipe is within crafting grid dimensions
        if (shapedRecipe.getGrid().getHeight() > inventory.getGridHeight() || shapedRecipe.getGrid().getWidth() > inventory.getGridWidth()) {
            return Optional.empty();
        }

        // We have a 3x3 grid, but the shaped recipe can be formed anywhere in that grid. Attempt every offset.
        for (int xOffset = 0; xOffset <= 3 - shapedRecipe.getGrid().getWidth(); xOffset++) {
            for (int yOffset = 0; yOffset <= 3 - shapedRecipe.getGrid().getHeight(); yOffset++) {
                // Using these offsets... try and see if every grid item matches!

                boolean isValidRecipeOffset = true;
                boolean isReverseValidRecipeOffset = true;
                for (int x = 0; x < shapedRecipe.getGrid().getWidth(); x++) {
                    for (int y = 0; y < shapedRecipe.getGrid().getHeight(); y++) {
                        Item requiredItem = shapedRecipe.getGrid().getItem(x, y);
                        Item reversedRequiredItem = shapedRecipe.getGrid().getItem(shapedRecipe.getGrid().getWidth() - x - 1, y);

                        int gridSlot = (y + yOffset) * inventory.getGridWidth() + (x + xOffset);
                        Item itemInGrid = inventory.getSlot(gridSlot);

                        if (!requiredItem.hasSameDataAs(itemInGrid) || requiredItem.getCount() > itemInGrid.getCount()) {
                            isValidRecipeOffset = false;
                        }

                        // Shaped recipes can be reversed horizontally too!
                        if (!reversedRequiredItem.hasSameDataAs(itemInGrid) || reversedRequiredItem.getCount() > itemInGrid.getCount()) {
                            isReverseValidRecipeOffset = false;
                        }
                    }
                }

                if (isValidRecipeOffset || isReverseValidRecipeOffset) {
                    return Optional.of(new ShapedRecipeOffsetData(xOffset, yOffset, isReverseValidRecipeOffset));
                }
            }
        }

        return Optional.empty();
    }

    private CraftingInventory getCraftingInventory(Player player) {
        return (CraftingInventory) player.getOpenInventory()
                .filter(inventory -> inventory instanceof CraftingInventory)
                .orElse(player.getInventory().getCraftingGrid());
    }


    private static class ShapedRecipeOffsetData {

        private final int xOffset;
        private final int yOffset;
        private final boolean reversed;


        public ShapedRecipeOffsetData(int xOffset, int yOffset, boolean reversed) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.reversed = reversed;
        }

        public int getXOffset() {
            return this.xOffset;
        }

        public int getYOffset() {
            return this.yOffset;
        }

        public boolean isReversed() {
            return this.reversed;
        }

    }

}
