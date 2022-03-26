package io.github.pizzaserver.server.player.handlers.inventory;

import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.type.Recipe;
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

        Recipe recipe = action.getRecipe().get();
        switch (recipe.getType()) {
            case SHAPELESS -> {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

                for (Item ingredient : shapelessRecipe.getIngredients()) {
                    if (!player.getInventory().getCraftingGrid().contains(ingredient)) {
                        return false;
                    }
                }

                return true;
            }
            case FURNACE, SHAPED -> {
                return false;
            }
            default -> {
                throw new NullPointerException("Unknown recipe type when trying to handle CRAFT_RECIPE: " + recipe.getType());
            }
        }
    }

    @Override
    protected boolean runAction(Player player, CraftRecipeRequestActionDataWrapper action) {
        Recipe recipe = action.getRecipe().get();

        switch (recipe.getType()) {
            case SHAPELESS -> {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

                for (Item ingredient : shapelessRecipe.getIngredients()) {
                    player.getInventory().getCraftingGrid().removeItem(ingredient);
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

}
