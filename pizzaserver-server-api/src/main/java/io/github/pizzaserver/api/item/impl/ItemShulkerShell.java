package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemShulkerShell extends Item {

    public ItemShulkerShell() {
        this(1);
    }

    public ItemShulkerShell(int count) {
        super(ItemID.SHULKER_SHELL, count);
    }

    @Override
    public String getName() {
        return "Shulker Shell";
    }
}
