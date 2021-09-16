package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources;

public enum InventoryTransactionSourceType {
    CONTAINER(0),
    GLOBAL(1),
    WORLD(2),
    CREATIVE(3),
    UNTRACKED(100),
    NOT_IMPLEMENTED(999999);

    private final int id;


    InventoryTransactionSourceType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static InventoryTransactionSourceType getById(int actionTypeId) {
        for (InventoryTransactionSourceType type : InventoryTransactionSourceType.values()) {
            if (type.getId() == actionTypeId) {
                return type;
            }
        }
        return null;
    }
}
