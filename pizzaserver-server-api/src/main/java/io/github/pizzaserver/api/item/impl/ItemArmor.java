package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ArmorItem;

public abstract class ItemArmor extends BaseItem implements ArmorItem, DurableItem {

    public ItemArmor(String itemId) {
        this(itemId, 1);
    }

    public ItemArmor(String itemId, int count) {
        super(itemId, count);
    }

    public ItemArmor(String itemId, int count, int meta) {
        super(itemId, count, meta);
    }

}
