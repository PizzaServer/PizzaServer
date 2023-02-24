package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemHoneyComb extends BaseItem {

    public ItemHoneyComb() {
        this(1);
    }

    public ItemHoneyComb(int count) {
        super(ItemID.FEATHER, count);
    }

    @Override
    public String getName() {
        return "Honeycomb";
    }

}
