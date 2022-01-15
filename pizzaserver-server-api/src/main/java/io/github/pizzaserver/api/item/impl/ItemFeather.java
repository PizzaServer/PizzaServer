package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemFeather extends Item {

    public ItemFeather() {
        this(1);
    }

    public ItemFeather(int count) {
        super(ItemID.FEATHER, count);
    }

    @Override
    public String getName() {
        return "Feather";
    }

}
