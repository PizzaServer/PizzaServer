package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemOakBoat extends ItemBaseBoat {

    public ItemOakBoat() {
        this(1);
    }

    public ItemOakBoat(int count) {
        super(ItemID.OAK_BOAT, WoodType.OAK, count);
    }

    @Override
    public String getName() {
        return "Boat";
    }

}
