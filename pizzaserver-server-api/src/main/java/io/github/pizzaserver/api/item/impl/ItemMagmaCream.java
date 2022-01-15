package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemMagmaCream extends Item {

    public ItemMagmaCream() {
        this(1);
    }

    public ItemMagmaCream(int count) {
        super(ItemID.MAGMA_CREAM, count);
    }

    @Override
    public String getName() {
        return "Magma Cream";
    }

}
