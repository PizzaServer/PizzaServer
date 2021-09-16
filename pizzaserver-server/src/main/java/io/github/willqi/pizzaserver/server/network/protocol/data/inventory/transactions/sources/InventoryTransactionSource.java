package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

/**
 * Used in inventory transactions
 * Represents the location of an inventory action
 */
public interface InventoryTransactionSource {

   InventoryTransactionSourceType getType();

}
