package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemPotato extends BaseItem implements FoodItem {

    public ItemPotato() {
        this(1);
    }

    public ItemPotato(int count) {
        super(ItemID.POTATO, count);
    }

    @Override
    public String getName() {
        return "Potato";
    }

    @Override
    public int getNutrition() {
        return 1;
    }

    @Override
    public float getSaturation() {
        return 0.6f;
    }
}
