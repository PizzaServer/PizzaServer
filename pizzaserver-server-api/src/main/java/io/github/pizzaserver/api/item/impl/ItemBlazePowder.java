package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBlazePowder extends Item {

    public ItemBlazePowder() {
        this(1);
    }

    public ItemBlazePowder(int count) {
        super(ItemID.BLAZE_POWDER, count);
    }

    @Override
    public String getName() {
        return "Blaze Powder";
    }

}
