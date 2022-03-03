package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FuelItem;

public class ItemCoal extends BaseItem implements FuelItem {

    public ItemCoal() {
        this(1);
    }

    public ItemCoal(int count) {
        super(ItemID.COAL, count);
    }

    @Override
    public String getName() {
        return "Coal";
    }

    @Override
    public int getFuelTicks() {
        return 1600;
    }

}
