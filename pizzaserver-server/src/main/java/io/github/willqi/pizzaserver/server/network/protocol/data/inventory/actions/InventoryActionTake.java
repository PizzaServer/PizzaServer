package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlotData;

/**
 * Used for server authoritative inventories
 * Created when a player tries to take an item from an inventory
 */
public class InventoryActionTake implements InventoryAction {

    private final InventorySlotData source;
    private final InventorySlotData destination;
    private final int count;


    public InventoryActionTake(InventorySlotData source, InventorySlotData destination, int count) {
        this.source = source;
        this.destination = destination;
        this.count = count;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.TAKE;
    }

    public InventorySlotData getSource() {
        return this.source;
    }

    public InventorySlotData getDestination() {
        return this.destination;
    }

    public int getCount() {
        return this.count;
    }

}
