package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

import java.util.Optional;

public class ItemMushroomStew extends BaseItem implements FoodItem {

    public ItemMushroomStew() {
        this(1);
    }

    public ItemMushroomStew(int count) {
        super(ItemID.MUSHROOM_STEW, count);
    }

    @Override
    public Optional<Item> getResultItem() {
        return Optional.of(new ItemBowl(1));
    }

    @Override
    public String getName() {
        return "Mushroom Stew";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 7.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
