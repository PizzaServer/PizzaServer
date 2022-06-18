package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawCod extends BaseItem implements FoodItem {

    public ItemRawCod() {
        this(1);
    }

    public ItemRawCod(int count) {
        super(ItemID.RAW_COD, count);
    }

    @Override
    public String getName() {
        return "Raw Cod";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 0.4f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
