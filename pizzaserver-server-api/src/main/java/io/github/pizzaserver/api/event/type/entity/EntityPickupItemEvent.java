package io.github.pizzaserver.api.event.type.entity;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;

/**
 * Called when an entity is about to pick up an item.
 */
public class EntityPickupItemEvent extends BaseEntityEvent.Cancellable {

    protected EntityItem pickedUpEntity;
    protected int pickedUpCount;

    public EntityPickupItemEvent(Entity entity, EntityItem pickedUpEntity, int pickedUpCount) {
        super(entity);
        this.pickedUpEntity = pickedUpEntity;
        this.pickedUpCount = pickedUpCount;
    }

    public EntityItem getItemEntity() {
        return this.pickedUpEntity;
    }

    public Item getItem() {
        return this.pickedUpEntity.getItem();
    }

    public int getPickedUpCount() {
        return this.pickedUpCount;
    }

    public void setPickedUpCount(int pickedUpCount) {
        this.pickedUpCount = pickedUpCount;
    }
}
