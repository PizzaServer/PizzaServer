package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemJungleBoat extends ItemBaseBoat {

    public ItemJungleBoat() {
        this(1);
    }

    public ItemJungleBoat(int count) {
        super(ItemID.JUNGLE_BOAT, WoodType.JUNGLE, count);
    }

    @Override
    public String getName() {
        return "Jungle Boat";
    }

}
