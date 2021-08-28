package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlotData;

/**
 * Used for server authoritative inventories
 * Created when a player tries to destroy an item by dragging it into the creative inventory
 */
public class InventoryActionDestroy implements InventoryAction {

    private final InventorySlotData source;
    private final int amount;


    public InventoryActionDestroy(InventorySlotData source, int amount) {
        this.source = source;
        this.amount = amount;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.DESTROY;
    }

    public InventorySlotData getSlot() {
        return this.source;
    }

    public int getAmount() {
        return this.amount;
    }

}
