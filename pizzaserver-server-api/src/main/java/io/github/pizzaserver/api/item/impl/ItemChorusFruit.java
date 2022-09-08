package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemChorusFruit extends BaseItem implements FoodItem {

    public ItemChorusFruit() {
        this(1);
    }

    public ItemChorusFruit(int count) {
        super(ItemID.CHORUS_FRUIT, count);
    }

    @Override
    public String getName() {
        return "Chorus Fruit";
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 2.4f;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: Implement later
    }
}
