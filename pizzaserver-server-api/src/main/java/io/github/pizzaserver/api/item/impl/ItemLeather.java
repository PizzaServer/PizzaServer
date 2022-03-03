package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLeather extends BaseItem {

    public ItemLeather() {
        this(1);
    }

    public ItemLeather(int count) {
        super(ItemID.LEATHER, count);
    }

    @Override
    public String getName() {
        return "Leather";
    }

}
