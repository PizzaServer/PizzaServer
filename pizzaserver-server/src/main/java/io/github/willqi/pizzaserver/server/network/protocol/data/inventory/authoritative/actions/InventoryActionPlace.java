package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;

/**
 * Used for server authoritative inventories.
 * Created when a player tries to place x amount of an item to another slot
 */
public class InventoryActionPlace implements InventoryAction {

    private final AuthoritativeInventorySlot source;
    private final AuthoritativeInventorySlot destination;
    private final int count;


    public InventoryActionPlace(AuthoritativeInventorySlot source, AuthoritativeInventorySlot destination, int count) {
        this.source = source;
        this.destination = destination;
        this.count = count;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.PLACE;
    }

    public AuthoritativeInventorySlot getSource() {
        return this.source;
    }

    public AuthoritativeInventorySlot getDestination() {
        return this.destination;
    }

    public int getCount() {
        return this.count;
    }

}
