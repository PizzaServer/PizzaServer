package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.item.descriptors.PlantableItem;

public class ItemCarrot extends BaseItem implements FoodItem, PlantableItem {

    public ItemCarrot() {
        this(1);
    }

    public ItemCarrot(int count) {
        super(ItemID.CARROT, count);
    }

    @Override
    public String getName() {
        return "Carrot";
    }

    @Override
    public int getNutrition() {
        return 3;
    }

    @Override
    public float getSaturation() {
        return 3.6f;
    }

    @Override
    public int getUseDurationTicks() {
        return 0;
    }

    @Override
    public Block getPlacedBlock() {
        return null;
    }

    @Override
    public Block[] getPlaceableBlockTypes() {
        return new Block[0];
    }
}
