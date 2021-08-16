package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;

public class ItemStack {

    public ItemStack(BaseItemType itemType) {
        this(itemType, 1);
    }

    public ItemStack(BaseItemType itemType, int amount) {
        this(itemType, amount, 0);
    }

    public ItemStack(BaseItemType itemType, int amount, int damage) {

    }

}
