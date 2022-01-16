package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemSpruceBoat extends ItemBaseBoat {

    public ItemSpruceBoat() {
        this(1);
    }

    public ItemSpruceBoat(int count) {
        super(ItemID.SPRUCE_BOAT, WoodType.SPRUCE, count);
    }

    @Override
    public String getName() {
        return "Spruce Boat";
    }
}
