package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemAcaciaBoat extends ItemBaseBoat {

    public ItemAcaciaBoat() {
        this(1);
    }

    public ItemAcaciaBoat(int count) {
        super(ItemID.ACACIA_BOAT, WoodType.ACACIA, count);
    }

    @Override
    public String getName() {
        return "Acacia Boat";
    }

}
