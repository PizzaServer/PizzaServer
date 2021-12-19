package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionCreativeSource implements InventoryTransactionSource {

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.CREATIVE;
    }

}
