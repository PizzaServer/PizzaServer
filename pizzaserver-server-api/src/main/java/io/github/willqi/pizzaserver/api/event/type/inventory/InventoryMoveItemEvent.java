package io.github.willqi.pizzaserver.api.event.type.inventory;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.item.ItemStack;

/**
 * Called when an entity tries to move an item into an inventory slot
 */
public class InventoryMoveItemEvent extends BaseInventoryEvent.Cancellable {

    protected Entity entity;

    protected Action action;

    protected Inventory inventory;
    protected InventorySlotType slotType;
    protected int movedItemSlot;
    protected ItemStack movedItem;
    protected int movedItemCount;

    protected Inventory destinationInventory;
    protected InventorySlotType destinationSlotType;
    protected int destinationItemSlot;
    protected ItemStack destinationCurrentItemStack;

    public InventoryMoveItemEvent(Entity entity,
                                  Action action,
                                  Inventory inventory,
                                  InventorySlotType slotType,
                                  int movedItemSlot,
                                  ItemStack movedItem,
                                  int movedItemCount,
                                  Inventory destinationInventory,
                                  InventorySlotType destinationSlotType,
                                  int destinationItemSlot,
                                  ItemStack destinationItem) {
        super(inventory);
        this.entity = entity;

        this.action = action;

        this.inventory = inventory;
        this.slotType = slotType;
        this.movedItemSlot = movedItemSlot;
        this.movedItem = movedItem;
        this.movedItemCount = movedItemCount;

        this.destinationInventory = destinationInventory;
        this.destinationSlotType = destinationSlotType;
        this.destinationItemSlot = destinationItemSlot;
        this.destinationCurrentItemStack = destinationItem;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Action getAction() {
        return this.action;
    }

    public InventorySlotType getMovedSlotType() {
        return this.slotType;
    }

    public int getMovedItemSlot() {
        return this.movedItemSlot;
    }

    public ItemStack getMovedItem() {
        return this.movedItem;
    }

    public int getMovedItemCount() {
        return this.movedItemCount;
    }

    public Inventory getTargetInventory() {
        return this.destinationInventory;
    }

    public InventorySlotType getTargetSlotType() {
        return this.destinationSlotType;
    }

    public int getTargetSlot() {
        return this.destinationItemSlot;
    }

    public ItemStack getTargetItem() {
        return this.destinationCurrentItemStack;
    }


    public enum Action {
        TAKE,
        PLACE,
        SWAP
    }

}
