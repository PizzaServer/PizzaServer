package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.inventory.CraftingInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.type.ShapedRecipe;
import io.github.pizzaserver.api.recipe.type.ShapelessRecipe;
import io.github.pizzaserver.server.inventory.ImplPlayerCraftingInventory;
import io.github.pizzaserver.server.network.data.inventory.actions.CraftRecipeRequestActionDataWrapper;

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

                // We have a 3x3 grid, but the shaped recipe can be formed anywhere in that grid. Attempt every offset.
                for (int xOffset = 0; xOffset <= 3 - shapedRecipe.getGrid().getWidth(); xOffset++) {
                    for (int yOffset = 0; yOffset <= 3 - shapedRecipe.getGrid().getHeight(); yOffset++) {
                        // Using these offsets... try and see if every grid item matches!
                        for (int x = 0; x < shapedRecipe.getGrid().getWidth(); x++) {
                            for (int y = 0; y < shapedRecipe.getGrid().getHeight(); y++) {
                                Item requiredItem = shapedRecipe.getGrid().getItem(x, y);
                                // TODO: this cannot be finished without a way to differentiate between a crafting table and a crafting grid.
                            }
                        }
                    }
                }

                return false;
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
                    if (excess.isPresent()) {
                        InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(player.getInventory(), player, excess.get());
                        player.getServer().getEventManager().call(dropItemEvent);

                        if (!dropItemEvent.isCancelled()) {
                            Item droppedItem = dropItemEvent.getDrop();

                            EntityItem itemEntity = EntityRegistry.getInstance().getItemEntity(droppedItem);
                            itemEntity.setPickupDelay(40);
                            player.getWorld().addItemEntity(itemEntity, player.getLocation().toVector3f().add(0, 1.3f, 0), player.getDirectionVector().mul(0.25f, 0.6f, 0.25f));
                        }
                    }
                }
            }
        }
        return true;
    }

    private CraftingInventory getCraftingInventory(Player player) {
        return (CraftingInventory) player.getOpenInventory()
                .filter(inventory -> inventory instanceof CraftingInventory)
                .orElse(player.getInventory().getCraftingGrid());
    }

}
