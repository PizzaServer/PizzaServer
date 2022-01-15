package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetherStar extends Item {

    public ItemNetherStar() {
        this(1);
    }

    public ItemNetherStar(int count) {
        super(ItemID.NETHER_STAR, count);
    }

    @Override
    public String getName() {
        return "Nether Star";
    }

}
