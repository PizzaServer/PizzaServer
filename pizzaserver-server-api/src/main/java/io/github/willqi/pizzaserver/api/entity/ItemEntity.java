package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.item.ItemStack;

public interface ItemEntity extends Entity {

    ItemStack getItem();

    void setItem(ItemStack item);

}
