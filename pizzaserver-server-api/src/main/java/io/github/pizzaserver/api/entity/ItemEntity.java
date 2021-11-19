package io.github.pizzaserver.api.entity;

import io.github.pizzaserver.api.item.ItemStack;

public interface ItemEntity extends Entity {

    ItemStack getItem();

    void setItem(ItemStack item);

    void setPickupDelay(int ticks);

    int getPickupDelay();

}
