package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteScrap extends Item {

    public ItemNetheriteScrap() {
        this(1);
    }

    public ItemNetheriteScrap(int count) {
        super(ItemID.NETHERITE_SCRAP, count);
    }

    @Override
    public String getName() {
        return "Netherite Scrap";
    }

}
