package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemPrismarineCrystals extends Item {

    public ItemPrismarineCrystals() {
        this(1);
    }

    public ItemPrismarineCrystals(int count) {
        super(ItemID.PRISMARINE_CRYSTALS, count);
    }

    @Override
    public String getName() {
        return "Prismarine Crystals";
    }

}
