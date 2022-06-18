package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawPorkchop extends BaseItem implements FoodItem {

    public ItemRawPorkchop() {
        this(1);
    }

    public ItemRawPorkchop(int count) {
        super(ItemID.RAW_PORKCHOP, count);
    }

    @Override
    public String getName() {
        return "Raw Porkchop";
    }

    @Override
    public int getNutrition() {
        return 3;
    }

    @Override
    public float getSaturation() {
        return 1.8f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
