package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedBeef extends BaseItem implements FoodItem {

    public ItemCookedBeef() {
        this(1);
    }

    public ItemCookedBeef(int count) {
        super(ItemID.COOKED_BEEF, count);
    }

    @Override
    public String getName() {
        return "Cooked Beef";
    }

    @Override
    public int getNutrition() {
        return 8;
    }

    @Override
    public float getSaturation() {
        return 12.8f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
