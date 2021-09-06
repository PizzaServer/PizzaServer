package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionContainerSource implements InventoryTransactionSource {

    private final int inventoryId;


    public InventoryTransactionContainerSource(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.CONTAINER;
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

}
