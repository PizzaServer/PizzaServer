package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemStick extends Item {

    public ItemStick() {
        this(1);
    }

    public ItemStick(int count) {
        super(ItemID.STICK, count);
    }

    @Override
    public String getName() {
        return "Stick";
    }
}
