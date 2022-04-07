package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.inventory.CraftingInventory;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.type.Recipe;

/**
 * Called when a player attempts to craft a recipe.
 */
public class PlayerCraftEvent extends BasePlayerEvent.Cancellable {

    protected final CraftingInventory craftingInventory;
    protected final Recipe recipe;

    public PlayerCraftEvent(Player player, CraftingInventory craftingInventory, Recipe recipe) {
        super(player);
        this.craftingInventory = craftingInventory;
        this.recipe = recipe;
    }

    public CraftingInventory getCraftingInventory() {
        return this.craftingInventory;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

}
