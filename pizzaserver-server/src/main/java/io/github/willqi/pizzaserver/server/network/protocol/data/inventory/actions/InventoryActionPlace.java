package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlot;

/**
 * Used for server authoritative inventories
 * Created when a player tries to place x amount of an item to another slot
 */
public class InventoryActionPlace implements InventoryAction {

    private final InventorySlot source;
    private final InventorySlot destination;
    private final int count;


    public InventoryActionPlace(InventorySlot source, InventorySlot destination, int count) {
        this.source = source;
        this.destination = destination;
        this.count = count;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.PLACE;
    }

    public InventorySlot getSource() {
        return this.source;
    }

    public InventorySlot getDestination() {
        return this.destination;
    }

    public int getCount() {
        return this.count;
    }

}
