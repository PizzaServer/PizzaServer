package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemClock extends BaseItem {

    public ItemClock() {
        this(1);
    }

    public ItemClock(int count) {
        super(ItemID.CLOCK, count);
    }

    @Override
    public String getName() {
        return "Clock";
    }

}
