package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemRawCopper extends Item {

    public ItemRawCopper() {
        this(1);
    }

    public ItemRawCopper(int count) {
        super(ItemID.RAW_COPPER, count);
    }

    @Override
    public String getName() {
        return "Raw Copper";
    }
}
