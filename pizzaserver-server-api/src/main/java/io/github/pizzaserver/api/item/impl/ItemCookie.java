package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemCookie extends BaseItem implements FoodItem {

    public ItemCookie() {
        this(1);
    }

    public ItemCookie(int count) {
        super(ItemID.COOKIE, count);
    }

    @Override
    public String getName() {
        return "Cookie";
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
