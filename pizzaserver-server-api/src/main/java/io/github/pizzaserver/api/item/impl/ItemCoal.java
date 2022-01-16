package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FuelItemComponent;

public class ItemCoal extends Item implements FuelItemComponent {

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
