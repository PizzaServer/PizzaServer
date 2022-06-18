package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

import java.util.Optional;

public class ItemBeetrootSoup extends BaseItem implements FoodItem {

    public ItemBeetrootSoup() {
        this(1);
    }

    public ItemBeetrootSoup(int count) {
        super(ItemID.BEETROOT_SOUP, count);
    }

    @Override
    public String getName() {
        return "Beetroot Soup";
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
    public Optional<Item> getResultItem() {
        return Optional.of(new ItemBowl());
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
