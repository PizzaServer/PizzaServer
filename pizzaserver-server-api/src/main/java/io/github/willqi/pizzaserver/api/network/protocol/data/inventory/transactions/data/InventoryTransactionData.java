package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.data;

import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.InventoryTransactionType;

/**
 * Used in inventory transactions.
 * Certain {@link InventoryTransactionType} have additional data passed in the transaction
 */
public interface InventoryTransactionData {

    InventoryTransactionType getType();

}
