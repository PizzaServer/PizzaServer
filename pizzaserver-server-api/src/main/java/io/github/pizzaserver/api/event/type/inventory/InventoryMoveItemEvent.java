package io.github.pizzaserver.api.event.type.inventory;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.inventory.Inventory;
import io.github.pizzaserver.api.item.Item;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;

/**
 * Called when an entity tries to move an item into an inventory slot.
 */
public class InventoryMoveItemEvent extends BaseInventoryEvent.Cancellable {

    protected Entity entity;

    protected ItemStackRequestActionType action;

    protected Inventory inventory;
    protected ContainerSlotType slotType;
    protected int movedItemSlot;
    protected Item movedItem;
    protected int movedItemCount;

    protected Inventory destinationInventory;
    protected ContainerSlotType destinationSlotType;
    protected int destinationItemSlot;
    protected Item destinationCurrentItemStack;

    public InventoryMoveItemEvent(Entity entity,
                                  ItemStackRequestActionType action,
                                  Inventory inventory,
                                  ContainerSlotType slotType,
                                  int movedItemSlot,
                                  Item movedItem,
                                  int movedItemCount,
                                  Inventory destinationInventory,
                                  ContainerSlotType destinationSlotType,
                                  int destinationItemSlot,
                                  Item destinationItem) {
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

    public ItemStackRequestActionType getAction() {
        return this.action;
    }

    public ContainerSlotType getMovedSlotType() {
        return this.slotType;
    }

    public int getMovedItemSlot() {
        return this.movedItemSlot;
    }

    public Item getMovedItem() {
        return this.movedItem.clone();
    }

    public int getMovedItemCount() {
        return this.movedItemCount;
    }

    public Inventory getTargetInventory() {
        return this.destinationInventory;
    }

    public ContainerSlotType getTargetSlotType() {
        return this.destinationSlotType;
    }

    public int getTargetSlot() {
        return this.destinationItemSlot;
    }

    public Item getTargetItem() {
        return this.destinationCurrentItemStack.clone();
    }

}
