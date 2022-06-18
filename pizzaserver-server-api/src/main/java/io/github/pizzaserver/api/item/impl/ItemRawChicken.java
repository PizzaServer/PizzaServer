package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemRawChicken extends BaseItem implements FoodItem {

    public ItemRawChicken() {
        this(1);
    }

    public ItemRawChicken(int count) {
        super(ItemID.RAW_CHICKEN, count);
    }

    @Override
    public String getName() {
        return "Raw Chicken";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 3.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: 30% chance for Hunger 1 for 30 seconds
    }
}
