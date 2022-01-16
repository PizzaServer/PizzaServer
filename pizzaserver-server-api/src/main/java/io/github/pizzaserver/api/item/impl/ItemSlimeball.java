package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemSlimeball extends Item {

    public ItemSlimeball() {
        this(1);
    }

    public ItemSlimeball(int count) {
        super(ItemID.SLIMEBALL, count);
    }

    @Override
    public String getName() {
        return "Slimeball";
    }
}
