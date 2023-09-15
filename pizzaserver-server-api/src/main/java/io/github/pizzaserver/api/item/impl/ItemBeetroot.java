package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemBeetroot extends BaseItem implements FoodItem {

    public ItemBeetroot() {
        this(1);
    }

    public ItemBeetroot(int count) {
        super(ItemID.BEETROOT, count);
    }

    @Override
    public String getName() {
        return "Beetroot";
    }

    @Override
    public int getNutrition() {
        return 1;
    }

    @Override
    public float getSaturation() {
        return 1.2f;
    }
}
