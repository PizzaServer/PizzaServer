package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBoat extends ItemBaseBoat {

    public ItemBoat() {
        this(1);
    }

    public ItemBoat(int count) {
        super(ItemID.BOAT, WoodType.OAK, count);
    }

    @Override
    public String getName() {
        return "Boat";
    }
}
