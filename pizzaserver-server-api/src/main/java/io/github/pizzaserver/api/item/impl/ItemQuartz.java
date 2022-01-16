package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemQuartz extends Item {

    public ItemQuartz() {
        this(1);
    }

    public ItemQuartz(int count) {
        super(ItemID.QUARTZ, count);
    }

    @Override
    public String getName() {
        return "Quartz";
    }
}
