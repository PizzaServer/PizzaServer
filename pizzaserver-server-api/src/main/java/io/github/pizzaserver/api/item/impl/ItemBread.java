package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemBread extends BaseItem implements FoodItem {

    public ItemBread() {
        this(1);
    }

    public ItemBread(int count) {
        super(ItemID.BREAD, count);
    }

    @Override
    public String getName() {
        return "Bread";
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
