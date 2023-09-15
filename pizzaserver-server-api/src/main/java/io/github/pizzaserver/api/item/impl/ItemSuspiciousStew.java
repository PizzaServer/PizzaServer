package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;

import java.util.Optional;

public class ItemSuspiciousStew extends BaseItem implements FoodItem {

    public ItemSuspiciousStew() {
        this(1);
    }

    public ItemSuspiciousStew(int count) {
        super(ItemID.SUSPICIOUS_STEW, count);
    }

    @Override
    public Optional<Item> getResultItem() {
        return Optional.of(new ItemBowl(1));
    }

    @Override
    public String getName() {
        return "Suspicious Stew";
    }

    @Override
    public int getNutrition() {
        return 6;
    }

    @Override
    public float getSaturation() {
        return 7.2f;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void onConsume(Player player) {
        //TODO: https://minecraft.fandom.com/wiki/Suspicious_Stew implement its effects
    }
}
