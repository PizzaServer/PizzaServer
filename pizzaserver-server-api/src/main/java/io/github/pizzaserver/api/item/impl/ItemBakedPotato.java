package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemBakedPotato extends BaseItem implements FoodItem {

    public ItemBakedPotato() {
        this(1);
    }

    public ItemBakedPotato(int count) {
        super(ItemID.BAKED_POTATO, count);
    }

    @Override
    public String getName() {
        return "Baked Potato";
    }

    @Override
    public int getNutrition() {
        return 5;
    }

    @Override
    public float getSaturation() {
        return 6;
    }
}
