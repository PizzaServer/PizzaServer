package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBirchBoat extends ItemBaseBoat {

    public ItemBirchBoat() {
        this(1);
    }

    public ItemBirchBoat(int count) {
        super(ItemID.BIRCH_BOAT, WoodType.BIRCH, count);
    }

    @Override
    public String getName() {
        return "Birch Boat";
    }

}
