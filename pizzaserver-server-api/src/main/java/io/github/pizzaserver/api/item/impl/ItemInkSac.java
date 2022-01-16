package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemInkSac extends Item {

    public ItemInkSac() {
        this(1);
    }

    public ItemInkSac(int count) {
        super(ItemID.INK_SAC, count);
    }

    @Override
    public String getName() {
        return "Ink Sac";
    }
}
