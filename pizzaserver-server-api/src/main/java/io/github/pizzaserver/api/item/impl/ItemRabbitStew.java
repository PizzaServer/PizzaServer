package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

import java.util.Optional;

public class ItemRabbitStew extends BaseItem implements FoodItem {

    public ItemRabbitStew() {
        this(1);
    }

    public ItemRabbitStew(int count) {
        super(ItemID.RABBIT_STEW, count);
    }

    @Override
    public Optional<Item> getResultItem() {
        return Optional.of(new ItemBowl(1));
    }

    @Override
    public String getName() {
        return "Rabbit Stew";
    }

    @Override
    public int getNutrition() {
        return 10;
    }

    @Override
    public float getSaturation() {
        return 12;
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
