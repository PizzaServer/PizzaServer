package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemPumpkinSeeds extends BaseItem {

    public ItemPumpkinSeeds() {
        this(1);
    }

    public ItemPumpkinSeeds(int count) {
        super(ItemID.PUMPKIN_SEEDS, count);
    }

    @Override
    public String getName() {
        return "Pumpkin Seeds";
    }
}
