package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlotData;

/**
 * Used for server authoritative inventories
 * Created for the items used up when a player crafts something (e.g. the wood from crafting planks)
 */
public class InventoryActionConsume implements InventoryAction {

    private final InventorySlotData source;
    private final int amount;


    public InventoryActionConsume(InventorySlotData source, int amount) {
        this.source = source;
        this.amount = amount;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.CONSUME;
    }

    public InventorySlotData getSlot() {
        return this.source;
    }

    public int getAmount() {
        return this.amount;
    }

}
