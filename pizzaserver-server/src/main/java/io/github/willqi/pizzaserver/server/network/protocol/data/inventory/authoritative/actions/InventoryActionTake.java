package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;

/**
 * Used for server authoritative inventories
 * Created when a player tries to take an item from an inventory
 */
public class InventoryActionTake implements InventoryAction {

    private final AuthoritativeInventorySlot source;
    private final AuthoritativeInventorySlot destination;
    private final int count;


    public InventoryActionTake(AuthoritativeInventorySlot source, AuthoritativeInventorySlot destination, int count) {
        this.source = source;
        this.destination = destination;
        this.count = count;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.TAKE;
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
