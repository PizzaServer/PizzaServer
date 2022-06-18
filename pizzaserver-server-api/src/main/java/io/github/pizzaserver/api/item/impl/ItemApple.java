package io.github.pizzaserver.api.item.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.UseAnimationType;
import io.github.pizzaserver.api.item.descriptors.FoodItem;

public class ItemApple extends BaseItem implements FoodItem {

    public ItemApple() {
        this(1);
    }

    public ItemApple(int count) {
        super(ItemID.APPLE, count);
    }

    @Override
    public String getName() {
        return "Apple";
    }

    @Override
    public float getSaturation() {
        return 2.4f;
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }
}
