package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGunpowder extends Item {

    public ItemGunpowder() {
        this(1);
    }

    public ItemGunpowder(int count) {
        super(ItemID.GUNPOWDER, count);
    }

    @Override
    public String getName() {
        return "Gunpowder";
    }

}
