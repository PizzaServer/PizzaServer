package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions;

public enum InventoryTransactionType {
    NORMAL,
    INVENTORY_MISMATCH,
    ITEM_USE,
    ITEM_USE_ON_ENTITY,
    ITEM_RELEASE
}
