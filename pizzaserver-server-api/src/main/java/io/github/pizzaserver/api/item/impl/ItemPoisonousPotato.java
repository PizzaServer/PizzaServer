package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemPoisonousPotato extends BaseItem implements FoodItem {

    public ItemPoisonousPotato() {
        this(1);
    }

    public ItemPoisonousPotato(int count) {
        super(ItemID.POISONOUS_POTATO, count);
    }

    @Override
    public String getName() {
        return "Poisonous Potato";
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 1.2f;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: 60% chance to get Poison 1 for 5 seconds
    }
}
