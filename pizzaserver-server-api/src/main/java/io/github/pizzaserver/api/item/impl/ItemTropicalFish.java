package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemTropicalFish extends BaseItem implements FoodItem {

    public ItemTropicalFish() {
        this(1);
    }

    public ItemTropicalFish(int count) {
        super(ItemID.TROPICAL_FISH, count);
    }

    @Override
    public String getName() {
        return "Tropical Fish";
    }

    @Override
    public int getNutrition() {
        return 1;
    }

    @Override
    public float getSaturation() {
        return 0.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
