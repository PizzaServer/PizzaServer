package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions;

/**
 * Used in inventory transactions.
 * Describes all slots that were affected by an inventory transactionn
 */
public class InventoryTransactionSlotChange {

    private final int inventoryId;
    private final byte[] changedSlots;


    public InventoryTransactionSlotChange(int inventoryId, byte[] changedSlots) {
        this.inventoryId = inventoryId;
        this.changedSlots = changedSlots;
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public byte[] getChangedSlots() {
        return this.changedSlots;
    }

}
