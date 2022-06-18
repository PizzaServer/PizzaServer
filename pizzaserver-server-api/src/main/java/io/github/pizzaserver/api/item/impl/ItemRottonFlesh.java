package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemRottonFlesh extends BaseItem implements FoodItem {

    public ItemRottonFlesh() {
        this(1);
    }

    public ItemRottonFlesh(int count) {
        super(ItemID.ROTTEN_FLESH, count);
    }

    @Override
    public String getName() {
        return "Rotten Flesh";
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 0.8f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: 80% chance Hunger 1 for 30 seconds
    }
}
