package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.DurableItem;

public class ItemStonePickaxe extends BaseItem implements DurableItem, ToolItem {

    public ItemStonePickaxe() {
        this(1);
    }

    public ItemStonePickaxe(int count) {
        this(count, 0);
    }

    public ItemStonePickaxe(int count, int meta) {
        super(ItemID.STONE_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Stone Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
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
