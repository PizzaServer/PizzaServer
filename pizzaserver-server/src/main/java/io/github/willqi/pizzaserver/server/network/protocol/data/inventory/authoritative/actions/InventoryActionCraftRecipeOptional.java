package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

/**
 * Used for server authoritative inventories. (v422+)
 * Used for crafting recipes that contain a name input
 */
public class InventoryActionCraftRecipeOptional implements InventoryAction {

    private final int networkId;
    private final int customNameIndex;

    public InventoryActionCraftRecipeOptional(int networkId, int customNameIndex) {
        this.networkId = networkId; // only used for cartography table
        this.customNameIndex = customNameIndex;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CRAFT_RECIPE_OPTIONAL;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    public int getCustomNameIndex() {
        return this.customNameIndex;
    }

}
