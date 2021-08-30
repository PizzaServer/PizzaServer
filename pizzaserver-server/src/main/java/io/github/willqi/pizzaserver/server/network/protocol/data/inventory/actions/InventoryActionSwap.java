package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlot;

/**
 * Used for server authoritative inventories
 * Created when a player tries to swap a slot with another slot
 */
public class InventoryActionSwap implements InventoryAction {

    private final InventorySlot source;
    private final InventorySlot destination;


    public InventoryActionSwap(InventorySlot source, InventorySlot destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.SWAP;
    }

    public InventorySlot getSource() {
        return this.source;
    }

    public InventorySlot getDestination() {
        return this.destination;
    }

}
