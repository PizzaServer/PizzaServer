package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedChicken extends BaseItem implements FoodItem {

    public ItemCookedChicken() {
        this(1);
    }

    public ItemCookedChicken(int count) {
        super(ItemID.COOKED_CHICKEN, count);
    }

    @Override
    public String getName() {
        return "Cooked Chicken";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 7.2f;
    }
}
