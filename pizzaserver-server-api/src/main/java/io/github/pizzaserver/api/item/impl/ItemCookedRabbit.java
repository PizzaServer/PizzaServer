package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedRabbit extends BaseItem implements FoodItem {

    public ItemCookedRabbit() {
        this(1);
    }

    public ItemCookedRabbit(int count) {
        super(ItemID.COOKED_RABBIT, count);
    }

    @Override
    public String getName() {
        return "Cooked Rabbit";
    }

    @Override
    public int getNutrition() {
        return 5;
    }

    @Override
    public float getSaturation() {
        return 6;
    }
}
