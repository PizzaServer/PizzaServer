package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

/**
 * Represents the location of an inventory action
 */
public interface InventoryTransactionSource {

   InventoryTransactionSourceType getType();

}
