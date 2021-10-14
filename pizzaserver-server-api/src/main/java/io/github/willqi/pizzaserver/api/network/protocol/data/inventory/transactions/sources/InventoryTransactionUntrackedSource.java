package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionUntrackedSource implements InventoryTransactionSource {

    private final int untrackedInventoryId;


    public InventoryTransactionUntrackedSource(int untrackedInventoryId) {
        this.untrackedInventoryId = untrackedInventoryId;
    }

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.UNTRACKED;
    }

    public int getUntrackedInventoryId() {
        return this.untrackedInventoryId;
    }

}
