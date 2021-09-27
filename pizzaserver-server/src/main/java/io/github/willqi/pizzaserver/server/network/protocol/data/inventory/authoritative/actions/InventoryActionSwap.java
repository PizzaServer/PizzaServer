package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;

/**
 * Used for server authoritative inventories.
 * Created when a player tries to swap a slot with another slot
 */
public class InventoryActionSwap implements InventoryAction {

    private final AuthoritativeInventorySlot source;
    private final AuthoritativeInventorySlot destination;


    public InventoryActionSwap(AuthoritativeInventorySlot source, AuthoritativeInventorySlot destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.SWAP;
    }

    public AuthoritativeInventorySlot getSource() {
        return this.source;
    }

    public AuthoritativeInventorySlot getDestination() {
        return this.destination;
    }

}
