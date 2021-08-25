package io.github.willqi.pizzaserver.server.network.protocol.data;

import io.github.willqi.pizzaserver.api.item.ItemStack;

/**
 * Wrapper for an item stack that has a runtime id assigned to it
 */
public class NetworkItemStackData {

    private final int runtimeId;
    private final ItemStack itemStack;

    public NetworkItemStackData(ItemStack itemStack, int runtimeId) {
        this.itemStack = itemStack;
        this.runtimeId = runtimeId;
    }

    /**
     * Item runtime id
     * @return item runtime id
     */
    public int getRuntimeId() {
        return this.runtimeId;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

}
