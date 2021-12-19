package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.data;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.InventoryTransactionType;

public class InventoryTransactionUseItemOnEntityData implements InventoryTransactionData {

    private final Action action;
    private final long entityRuntimeId;
    private final int hotbarSlot;
    private final ItemStack heldItem;
    private final Vector3 position;
    private final Vector3 clickedPosition;


    public InventoryTransactionUseItemOnEntityData(long entityRuntimeId,
                                                   Action action,
                                                   int hotbarSlot,
                                                   ItemStack heldItem,
                                                   Vector3 position,
                                                   Vector3 clickedPosition) {
        this.entityRuntimeId = entityRuntimeId;
        this.action = action;
        this.hotbarSlot = hotbarSlot;
        this.heldItem = heldItem;
        this.position = position;
        this.clickedPosition = clickedPosition;
    }

    @Override
    public InventoryTransactionType getType() {
        return InventoryTransactionType.ITEM_USE_ON_ENTITY;
    }

    public Action getAction() {
        return this.action;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public Vector3 getClickedPosition() {
        return this.clickedPosition;
    }


    public enum Action {
        INTERACT,
        ATTACK
    }

}
