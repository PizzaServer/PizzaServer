package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedCod extends BaseItem implements FoodItem {

    public ItemCookedCod() {
        this(1);
    }

    public ItemCookedCod(int count) {
        super(ItemID.COOKED_COD, count);
    }

    @Override
    public String getName() {
        return "Cooked Cod";
    }

    @Override
    public int getNutrition() {
        return 5;
    }

    @Override
    public float getSaturation() {
        return 6;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
