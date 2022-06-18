package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedMutton extends BaseItem implements FoodItem {

    public ItemCookedMutton() {
        this(1);
    }

    public ItemCookedMutton(int count) {
        super(ItemID.COOKED_MUTTON, count);
    }

    @Override
    public String getName() {
        return "Cooked Mutton";
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
