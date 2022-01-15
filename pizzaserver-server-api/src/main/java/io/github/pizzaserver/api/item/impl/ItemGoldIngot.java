package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldIngot extends Item {

    public ItemGoldIngot() {
        this(1);
    }

    public ItemGoldIngot(int count) {
        super(ItemID.GOLD_INGOT, count);
    }

    @Override
    public String getName() {
        return "Gold Ingot";
    }

}
