package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemGoldenCarrot extends BaseItem implements FoodItem {

    public ItemGoldenCarrot() {
        this(1);
    }

    public ItemGoldenCarrot(int count) {
        super(ItemID.GOLDEN_CARROT, count);
    }

    @Override
    public String getName() {
        return "Golden Carrot";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 14.4f;
    }
}
