package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamond extends Item {

    public ItemDiamond() {
        this(1);
    }

    public ItemDiamond(int count) {
        super(ItemID.DIAMOND, count);
    }

    @Override
    public String getName() {
        return "Diamond";
    }

}
