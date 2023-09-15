package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawSalmon extends BaseItem implements FoodItem {

    public ItemRawSalmon() {
        this(1);
    }

    public ItemRawSalmon(int count) {
        super(ItemID.RAW_SALMON, count);
    }

    @Override
    public String getName() {
        return "Raw Salmon";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 0.4f;
    }
}
