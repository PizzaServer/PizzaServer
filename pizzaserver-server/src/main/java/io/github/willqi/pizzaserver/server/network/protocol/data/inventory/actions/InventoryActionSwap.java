package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlotData;

/**
 * Used for server authoritative inventories
 * Created when a player tries to swap a slot with another slot
 */
public class InventoryActionSwap implements InventoryAction {

    private final InventorySlotData source;
    private final InventorySlotData destination;


    public InventoryActionSwap(InventorySlotData source, InventorySlotData destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.SWAP;
    }

    public InventorySlotData getSource() {
        return this.source;
    }

    public InventorySlotData getDestination() {
        return this.destination;
    }

}
