package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;

public abstract class ItemHorseArmor extends BaseItem {

    public ItemHorseArmor(String itemId) {
        super(itemId, 1);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
