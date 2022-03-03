package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBlazeRod extends BaseItem {

    public ItemBlazeRod() {
        this(1);
    }

    public ItemBlazeRod(int count) {
        super(ItemID.BLAZE_ROD, count);
    }

    @Override
    public String getName() {
        return "Blaze Rod";
    }

}
