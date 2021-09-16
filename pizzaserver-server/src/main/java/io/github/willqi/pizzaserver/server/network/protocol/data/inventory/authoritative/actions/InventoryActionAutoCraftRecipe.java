package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

/**
 * Used for server authoritative inventories
 * Created when a player tries to shift click a recipe to automatically craft it.
 * This action contains the recipe being crafted
 */
public class InventoryActionAutoCraftRecipe implements InventoryAction {

    private final int recipeNetworkId;


    public InventoryActionAutoCraftRecipe(int recipeNetworkId) {
        this.recipeNetworkId = recipeNetworkId;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.AUTO_CRAFT_RECIPE;
    }

    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

}
