package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDragonBreath extends Item {

    public ItemDragonBreath() {
        this(1);
    }

    public ItemDragonBreath(int count) {
        super(ItemID.DRAGON_BREATH, count);
    }

    @Override
    public String getName() {
        return "Dragon's Breath";
    }

}
