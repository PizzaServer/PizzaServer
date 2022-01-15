package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemFlint extends Item {

    public ItemFlint() {
        this(1);
    }

    public ItemFlint(int count) {
        super(ItemID.FLINT, count);
    }

    @Override
    public String getName() {
        return "Flint";
    }

}
