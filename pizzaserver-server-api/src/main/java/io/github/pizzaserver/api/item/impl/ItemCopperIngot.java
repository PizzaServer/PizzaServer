package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemCopperIngot extends Item {

    public ItemCopperIngot() {
        this(1);
    }

    public ItemCopperIngot(int count) {
        super(ItemID.COPPER_INGOT, count);
    }

    @Override
    public String getName() {
        return "Copper Ingot";
    }

}
