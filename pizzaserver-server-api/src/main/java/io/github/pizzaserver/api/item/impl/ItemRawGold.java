package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemRawGold extends BaseItem {

    public ItemRawGold() {
        this(1);
    }

    public ItemRawGold(int count) {
        super(ItemID.RAW_GOLD, count);
    }

    @Override
    public String getName() {
        return "Raw Gold";
    }

}
