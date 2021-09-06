package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionNotImplementedSource implements InventoryTransactionSource {

    private final int notImplementedInventoryId;


    public InventoryTransactionNotImplementedSource(int notImplementedInventoryId) {
        this.notImplementedInventoryId = notImplementedInventoryId;
    }

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.NOT_IMPLEMENTED;
    }

    public int getNotImplementedInventoryId() {
        return this.notImplementedInventoryId;
    }

}
