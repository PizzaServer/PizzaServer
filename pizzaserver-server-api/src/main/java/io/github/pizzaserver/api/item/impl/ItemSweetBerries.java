package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemSweetBerries extends BaseItem implements FoodItem {

    public ItemSweetBerries() {
        this(1);
    }

    public ItemSweetBerries(int count) {
        super(ItemID.SWEET_BERRIES, count);
    }

    @Override
    public String getName() {
        return "Sweet Berries";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        // The wiki says 0.4 for Java and 1.2 for Bedrock, in 1.18/1.17 though Bedrock did balance saturation to match
        // Java's, so it may be outdated
        return 0.4f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
