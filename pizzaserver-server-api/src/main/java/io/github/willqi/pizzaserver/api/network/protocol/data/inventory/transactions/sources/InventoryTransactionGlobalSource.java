package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionGlobalSource implements InventoryTransactionSource {

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.GLOBAL;
    }

}
