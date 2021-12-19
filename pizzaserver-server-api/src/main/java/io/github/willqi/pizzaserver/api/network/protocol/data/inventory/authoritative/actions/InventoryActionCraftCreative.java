package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions;

/**
 * Used for server authoritative inventories.
 * Created when a player retrieves an item from the creative menu
 * Contains the network id retrieved from the creative content packet
 */
public class InventoryActionCraftCreative implements InventoryAction {

    private final int creativeNetworkId;


    public InventoryActionCraftCreative(int creativeNetworkId) {
        this.creativeNetworkId = creativeNetworkId;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CRAFT_CREATIVE;
    }

    public int getCreativeNetworkId() {
        return this.creativeNetworkId;
    }

}
