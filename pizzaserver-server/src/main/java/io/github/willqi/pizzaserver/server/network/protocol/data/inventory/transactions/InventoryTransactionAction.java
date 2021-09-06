package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources.InventoryTransactionSource;

public class InventoryTransactionAction {
    private final InventoryTransactionSource source;
    private final ItemStack oldItemStack;
    private final ItemStack newItemStack;
    private int slot;


    public InventoryTransactionAction(InventoryTransactionSource source, int slot, ItemStack oldItemStack, ItemStack newItemStack) {
        this.source = source;
        this.slot = slot;
        this.oldItemStack = oldItemStack;
        this.newItemStack = newItemStack;
    }

    public InventoryTransactionSource getSource() {
        return this.source;
    }

    public ItemStack getOldItemStack() {
        return this.oldItemStack;
    }

    public ItemStack getNewItemStack() {
        return this.newItemStack;
    }

    public int getSlot() {
        return this.slot;
    }

}
