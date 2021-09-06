package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

public class InventoryTransactionWorldSource implements InventoryTransactionSource {

    private final Flag flag;


    public InventoryTransactionWorldSource(Flag flag) {
        this.flag = flag;
    }

    @Override
    public InventoryTransactionSourceType getType() {
        return InventoryTransactionSourceType.WORLD;
    }

    public Flag getFlag() {
        return this.flag;
    }


    public enum Flag {
        DROP_ITEM,
        PICKUP_ITEM,
        NONE
    }

}
