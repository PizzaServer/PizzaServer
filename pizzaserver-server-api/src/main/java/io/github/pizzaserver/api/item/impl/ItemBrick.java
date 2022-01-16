package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBrick extends Item {

    public ItemBrick() {
        this(1);
    }

    public ItemBrick(int count) {
        super(ItemID.BRICK, count);
    }

    @Override
    public String getName() {
        return "Brick";
    }
}
