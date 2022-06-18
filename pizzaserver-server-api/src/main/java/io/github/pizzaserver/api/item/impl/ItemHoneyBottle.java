package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

import java.util.Optional;

public class ItemHoneyBottle extends BaseItem implements FoodItem {

    public ItemHoneyBottle() {
        this(1);
    }

    public ItemHoneyBottle(int count) {
        super(ItemID.HONEY_BOTTLE, count);
    }

    @Override
    public Optional<Item> getResultItem() {
        return Optional.of(new ItemGlassBottle(1));
    }

    @Override
    public String getName() {
        return "Honey Bottle";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 1.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
