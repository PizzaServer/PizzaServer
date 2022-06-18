package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookedPorkchop extends BaseItem implements FoodItem {

    public ItemCookedPorkchop() {
        this(1);
    }

    public ItemCookedPorkchop(int count) {
        super(ItemID.COOKED_PORKCHOP, count);
    }

    @Override
    public String getName() {
        return "Cooked Porkchop";
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
