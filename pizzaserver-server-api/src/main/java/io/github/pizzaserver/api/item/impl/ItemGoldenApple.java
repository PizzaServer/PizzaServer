package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemGoldenApple extends BaseItem implements FoodItem {

    public ItemGoldenApple() {
        this(1);
    }

    public ItemGoldenApple(int count) {
        super(ItemID.GOLDEN_APPLE, count);
    }

    @Override
    public String getName() {
        return "Golden Apple";
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 9.6f;
    }

    @Override
    public void onConsume(Player player) {
        // TODO: Regen 2 for 5 mins and Absorption 1 for 2 mins
    }

    @Override
    public boolean canAlwaysBeEaten() {
        return true;
    }
}
