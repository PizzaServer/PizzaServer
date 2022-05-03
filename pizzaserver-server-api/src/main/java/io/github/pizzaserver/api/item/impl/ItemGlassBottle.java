package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGlassBottle extends BaseItem {

    public ItemGlassBottle() {
        this(1);
    }

    public ItemGlassBottle(int count) {
        super(ItemID.GLASS_BOTTLE, count);
    }

    @Override
    public String getName() {
        return "Glass Bottle";
    }

}
