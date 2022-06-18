package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemGlowBerries extends BaseItem implements FoodItem {

    public ItemGlowBerries() {
        this(1);
    }

    public ItemGlowBerries(int count) {
        super(ItemID.GLOW_BERRIES, count);
    }

    @Override
    public String getName() {
        return "Glow Berries";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 0.4f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
