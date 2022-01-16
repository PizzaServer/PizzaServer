package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemSugar extends Item {

    public ItemSugar() {
        this(1);
    }

    public ItemSugar(int count) {
        super(ItemID.SUGAR, count);
    }

    @Override
    public String getName() {
        return "Sugar";
    }
}
