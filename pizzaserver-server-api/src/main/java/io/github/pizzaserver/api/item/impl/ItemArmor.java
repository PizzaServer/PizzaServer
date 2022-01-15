package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ArmorItemComponent;

public abstract class ItemArmor extends Item implements ArmorItemComponent, DurableItemComponent {

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
