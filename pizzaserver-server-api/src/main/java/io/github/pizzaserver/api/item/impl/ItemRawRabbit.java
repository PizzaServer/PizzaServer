package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemRawRabbit extends BaseItem implements FoodItem {

    public ItemRawRabbit() {
        this(1);
    }

    public ItemRawRabbit(int count) {
        super(ItemID.RAW_RABBIT, count);
    }

    @Override
    public String getName() {
        return "Raw Rabbit";
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
