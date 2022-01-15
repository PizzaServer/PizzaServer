package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemClayBall extends Item {

    public ItemClayBall() {
        this(1);
    }

    public ItemClayBall(int count) {
        super(ItemID.CLAY_BALL, count);
    }

    @Override
    public String getName() {
        return "Clay Ball";
    }

}
