package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNautilusShell extends Item {

    public ItemNautilusShell() {
        this(1);
    }

    public ItemNautilusShell(int count) {
        super(ItemID.NAUTILUS_SHELL, count);
    }

    @Override
    public String getName() {
        return "Nautilus Shell";
    }
}
