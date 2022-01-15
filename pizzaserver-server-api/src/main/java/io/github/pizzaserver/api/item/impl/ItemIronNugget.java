package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronNugget extends Item {

    public ItemIronNugget() {
        this(1);
    }

    public ItemIronNugget(int count) {
        super(ItemID.IRON_NUGGET, count);
    }

    @Override
    public String getName() {
        return "Iron Ingot";
    }

}
