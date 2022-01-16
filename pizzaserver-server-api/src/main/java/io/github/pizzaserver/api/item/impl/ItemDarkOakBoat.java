package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDarkOakBoat extends ItemBaseBoat {

    public ItemDarkOakBoat() {
        this(1);
    }

    public ItemDarkOakBoat(int count) {
        super(ItemID.DARK_OAK_BOAT, WoodType.DARK_OAK, count);
    }

    @Override
    public String getName() {
        return "Dark Oak Boat";
    }
}
