package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteIngot extends Item {

    public ItemNetheriteIngot() {
        this(1);
    }

    public ItemNetheriteIngot(int count) {
        super(ItemID.NETHERITE_INGOT, count);
    }

    @Override
    public String getName() {
        return "Netherite Ingot";
    }

}
