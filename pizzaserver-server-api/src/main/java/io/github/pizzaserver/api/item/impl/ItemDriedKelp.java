package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemDriedKelp extends BaseItem implements FoodItem {

    public ItemDriedKelp() {
        this(1);
    }

    public ItemDriedKelp(int count) {
        super(ItemID.DRIED_KELP, count);
    }

    @Override
    public String getName() {
        return "Dried Kelp";
    }

    @Override
    public int getNutrition() {
        return 1;
    }

    @Override
    public float getSaturation() {
        // The wiki says 0.6 for Java and 0.2 for Bedrock, in 1.18/1.17 though Bedrock did balance saturation to match
        // Java's, so it may be outdated
        return 0.6f;
    }

    @Override
    public int getUseDurationTicks() {
        return 16;
    }
}