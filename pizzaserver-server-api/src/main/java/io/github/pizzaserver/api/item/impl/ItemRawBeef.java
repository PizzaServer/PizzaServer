package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawBeef extends BaseItem implements FoodItem {

    public ItemRawBeef() {
        this(1);
    }

    public ItemRawBeef(int count) {
        super(ItemID.RAW_BEEF, count);
    }

    @Override
    public String getName() {
        return "Raw Beef";
    }

    @Override
    public int getNutrition() {
        return 3;
    }

    @Override
    public float getSaturation() {
        return 1.8f;
    }
}
