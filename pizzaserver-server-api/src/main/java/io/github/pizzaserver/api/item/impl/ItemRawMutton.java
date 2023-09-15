package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawMutton extends BaseItem implements FoodItem {

    public ItemRawMutton() {
        this(1);
    }

    public ItemRawMutton(int count) {
        super(ItemID.RAW_MUTTON, count);
    }

    @Override
    public String getName() {
        return "Raw Mutton";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 1.2f;
    }
}
