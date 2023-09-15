package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemPumpkinPie extends BaseItem implements FoodItem {

    public ItemPumpkinPie() {
        this(1);
    }

    public ItemPumpkinPie(int count) {
        super(ItemID.PUMPKIN_PIE, count);
    }

    @Override
    public String getName() {
        return "Pumpkin Pie";
    }

    @Override
    public int getNutrition() {
        return 8;
    }

    @Override
    public float getSaturation() {
        return 4.8f;
    }
}
