package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemDiamondSword extends BaseItem implements DurableItem, ToolItem {

    public ItemDiamondSword() {
        this(1);
    }

    public ItemDiamondSword(int count) {
        this(count, 0);
    }

    public ItemDiamondSword(int count, int meta) {
        super(ItemID.DIAMOND_SWORD, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 8;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.DIAMOND;
    }

    @Override
    public int getMaxDurability() {
        return 1562;
    }

}
