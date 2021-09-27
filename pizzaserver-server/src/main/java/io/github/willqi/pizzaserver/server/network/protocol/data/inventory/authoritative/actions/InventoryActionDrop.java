package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.AuthoritativeInventorySlot;

/**
 * Used for server authoritative inventories.
 * Created when a player tries to drop items out of their OPEN inventory
 */
public class InventoryActionDrop implements InventoryAction {

    private final AuthoritativeInventorySlot source;
    private final int amount;
    private final boolean randomly;


    public InventoryActionDrop(AuthoritativeInventorySlot source, int amount, boolean randomly) {
        this.source = source;
        this.amount = amount;
        this.randomly = randomly;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.DROP;
    }

    public AuthoritativeInventorySlot getSlot() {
        return this.source;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean isRandomly() {
        return this.randomly;
    }

}
