package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemEmerald extends Item {

    public ItemEmerald() {
        this(1);
    }

    public ItemEmerald(int count) {
        super(ItemID.EMERALD, count);
    }

    @Override
    public String getName() {
        return "Emerald";
    }

}
