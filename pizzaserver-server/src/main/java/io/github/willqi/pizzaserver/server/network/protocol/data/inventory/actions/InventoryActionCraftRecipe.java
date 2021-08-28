package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

/**
 * Used for server authoritative inventories
 * Created right before CREATE or CONSUME actions are sent.
 * This action contains the recipe being crafted
 */
public class InventoryActionCraftRecipe implements InventoryAction {

    private final int recipeNetworkId;


    public InventoryActionCraftRecipe(int recipeNetworkId) {
        this.recipeNetworkId = recipeNetworkId;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CRAFT_RECIPE;
    }

    public int getRecipeNetworkId() {
        return this.recipeNetworkId;
    }

}
