package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemRawIron extends Item {

    public ItemRawIron() {
        this(1);
    }

    public ItemRawIron(int count) {
        super(ItemID.RAW_IRON, count);
    }

    @Override
    public String getName() {
        return "Raw Iron";
    }

}
