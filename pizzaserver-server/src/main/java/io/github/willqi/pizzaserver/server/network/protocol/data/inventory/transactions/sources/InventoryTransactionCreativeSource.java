package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionCreativeSource implements InventoryTransactionSource {

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.CREATIVE;
    }

}
