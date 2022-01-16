package io.github.pizzaserver.server.network.data.inventory;

public class InventoryTransactionAction {

    private InventoryTransactionAction() { }

    public static final int USE_CLICK_BLOCK = 0;
    public static final int USE_CLICK_AIR = 1;
    public static final int USE_BREAK_BLOCK = 2;

    public static final int USE_ENTITY_INTERACT = 0;
    public static final int USE_ENTITY_ATTACK = 1;
}
