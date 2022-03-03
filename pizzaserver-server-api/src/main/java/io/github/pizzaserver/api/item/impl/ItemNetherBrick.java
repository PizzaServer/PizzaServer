package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetherBrick extends BaseItem {

    public ItemNetherBrick() {
        this(1);
    }

    public ItemNetherBrick(int count) {
        super(ItemID.NETHER_BRICK, count);
    }

    @Override
    public String getName() {
        return "Nether Brick";
    }

}
