package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FuelItem;

public class ItemCharcoal extends BaseItem implements FuelItem {

    public ItemCharcoal() {
        this(1);
    }

    public ItemCharcoal(int count) {
        super(ItemID.CHARCOAL, count);
    }

    @Override
    public String getName() {
        return "Charcoal";
    }

    @Override
    public int getFuelTicks() {
        return 1600;
    }

}
