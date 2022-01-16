package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.Item;

public abstract class ItemBaseBoat extends Item {

    private final WoodType woodType;


    public ItemBaseBoat(String itemId, WoodType woodType, int count) {
        super(itemId, count);
        this.woodType = woodType;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canUseOnLiquid() {
        return true;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }
}
