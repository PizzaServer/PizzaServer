package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions;

/**
 * Used for server authoritative inventories.
 * Created when a player tries to use a lab table
 */
public class InventoryActionLabTable implements InventoryAction {

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.LAB_TABLE_COMBINE;
    }

}
