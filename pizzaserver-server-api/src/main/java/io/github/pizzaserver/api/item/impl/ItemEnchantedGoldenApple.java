package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

public class ItemEnchantedGoldenApple extends BaseItem implements FoodItem {

    public ItemEnchantedGoldenApple() {
        this(1);
    }

    public ItemEnchantedGoldenApple(int count) {
        super(ItemID.ENCHANTED_GOLDEN_APPLE, count);
    }

    @Override
    public String getName() {
        return "Enchanted Golden Apple";
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
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public boolean canAlwaysBeEaten() {
        return true;
    }

    @Override
    public void onConsume(Player player) {
        // TODO: Regen 4 for 30 seconds, Absorption 4 for 2 mins, Res 1 for 5 mins, Fire Res for 5 mins
    }
}
