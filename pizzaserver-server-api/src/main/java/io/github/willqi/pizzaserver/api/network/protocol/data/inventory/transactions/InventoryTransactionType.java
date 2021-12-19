package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions;

/**
 * Used in inventory transactions.
 * All transactions have a type associated with them that determines the action that was done
 * and the data the transaction will contain
 */
public enum InventoryTransactionType {
    NORMAL,
    INVENTORY_MISMATCH,
    ITEM_USE,
    ITEM_USE_ON_ENTITY,
    ITEM_RELEASE
}
