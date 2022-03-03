package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemStoneSword extends BaseItem implements DurableItem, ToolItem {

    public ItemStoneSword() {
        this(1);
    }

    public ItemStoneSword(int count) {
        this(count, 0);
    }

    public ItemStoneSword(int count, int meta) {
        super(ItemID.STONE_SWORD, count, meta);
    }

    @Override
    public String getName() {
        return "Stone Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.STONE;
    }

    @Override
    public int getMaxDurability() {
        return 132;
    }

}
