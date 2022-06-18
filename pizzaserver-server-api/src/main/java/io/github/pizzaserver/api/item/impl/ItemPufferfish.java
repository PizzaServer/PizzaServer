package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemPufferfish extends BaseItem implements FoodItem {

    public ItemPufferfish() {
        this(1);
    }

    public ItemPufferfish(int count) {
        super(ItemID.PUFFERFISH, count);
    }

    @Override
    public String getName() {
        return "Pufferfish";
    }

    @Override
    public int getNutrition() {
        return 1;
    }

    @Override
    public float getSaturation() {
        return 0.2f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: Hunger 3 for 15 seconds, Nausea 1 for 15 seconds, Poison 2 for a minute
    }
}
