package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemMelonSlice extends BaseItem implements FoodItem {

    public ItemMelonSlice() {
        this(1);
    }

    public ItemMelonSlice(int count) {
        super(ItemID.MELON_SLICE, count);
    }

    @Override
    public String getName() {
        return "Melon Slice";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 1.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
