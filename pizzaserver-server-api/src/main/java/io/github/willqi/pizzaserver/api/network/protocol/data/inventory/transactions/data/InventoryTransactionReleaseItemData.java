package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.data;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.InventoryTransactionType;

public class InventoryTransactionReleaseItemData implements InventoryTransactionData {

    private final Action action;
    private final int hotbarSlot;
    private final ItemStack heldItem;
    private final Vector3 headPosition;

    public InventoryTransactionReleaseItemData(Action action, int hotbarSlot, ItemStack heldItem, Vector3 headPosition) {
        this.action = action;
        this.hotbarSlot = hotbarSlot;
        this.heldItem = heldItem;
        this.headPosition = headPosition;
    }

    @Override
    public InventoryTransactionType getType() {
        return InventoryTransactionType.ITEM_RELEASE;
    }

    public Action getAction() {
        return this.action;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public Vector3 getHeadPosition() {
        return this.headPosition;
    }


    public enum Action {
        RELEASE,
        CONSUME
    }

}