package io.github.pizzaserver.api.event.type.entity;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;

/**
 * Called when an entity is about to pick up an item.
 */
public class EntityPickupItemEvent extends BaseEntityEvent.Cancellable {

    protected ItemEntity pickedUpEntity;
    protected int pickedUpCount;

    public EntityPickupItemEvent(Entity entity, ItemEntity pickedUpEntity, int pickedUpCount) {
        super(entity);
        this.pickedUpEntity = pickedUpEntity;
        this.pickedUpCount = pickedUpCount;
    }

    public ItemEntity getItemEntity() {
        return this.pickedUpEntity;
    }

    public ItemStack getItem() {
        return this.pickedUpEntity.getItem();
    }

    public int getPickedUpCount() {
        return this.pickedUpCount;
    }

    public void setPickedUpCount(int pickedUpCount) {
        this.pickedUpCount = pickedUpCount;
    }

}
