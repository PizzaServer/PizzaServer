package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.data;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.api.network.protocol.data.inventory.transactions.InventoryTransactionType;

public class InventoryTransactionUseItemData implements InventoryTransactionData {

    private final Action action;
    private final Vector3i blockCoordinates;
    private final BlockFace blockFace;
    private final int hotbarSlot;
    private final ItemStack heldItem;
    private final Vector3 position;
    private final Vector3 clickedPosition;
    private final int blockRuntimeId;


    public InventoryTransactionUseItemData(Action action,
                                           Vector3i blockCoordinates,
                                           BlockFace blockFace,
                                           int hotbarSlot,
                                           ItemStack heldItem,
                                           Vector3 position,
                                           Vector3 clickedPosition,
                                           int blockRuntimeId) {
        this.action = action;
        this.blockCoordinates = blockCoordinates;
        this.blockFace = blockFace;
        this.hotbarSlot = hotbarSlot;
        this.heldItem = heldItem;
        this.position = position;
        this.clickedPosition = clickedPosition;
        this.blockRuntimeId = blockRuntimeId;
    }

    @Override
    public InventoryTransactionType getType() {
        return InventoryTransactionType.ITEM_USE;
    }

    public Action getAction() {
        return this.action;
    }

    public Vector3i getBlockCoordinates() {
        return this.blockCoordinates;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
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

    public int getBlockRuntimeId() {
        return this.blockRuntimeId;
    }


    public enum Action {
        CLICK_BLOCK,
        CLICK_AIR,
        BREAK_BLOCK
    }

}
