package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronIngot extends Item {

    public ItemIronIngot() {
        this(1);
    }

    public ItemIronIngot(int count) {
        super(ItemID.IRON_INGOT, count);
    }

    @Override
    public String getName() {
        return "Iron Ingot";
    }
}
