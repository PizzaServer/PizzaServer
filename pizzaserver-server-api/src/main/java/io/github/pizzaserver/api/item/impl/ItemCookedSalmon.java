package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedSalmon extends BaseItem implements FoodItem {

    public ItemCookedSalmon() {
        this(1);
    }

    public ItemCookedSalmon(int count) {
        super(ItemID.COOKED_SALMON, count);
    }

    @Override
    public String getName() {
        return "Cooked Salmon";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 9.6f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
