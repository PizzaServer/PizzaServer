package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemSpiderEye extends BaseItem implements FoodItem {

    public ItemSpiderEye() {
        this(1);
    }

    public ItemSpiderEye(int count) {
        super(ItemID.SPIDER_EYE, count);
    }

    @Override
    public String getName() {
        return "Spider Eye";
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
    public void onConsume(Player player) {
        //TODO: Poison 1 for 5 seconds
    }
}
