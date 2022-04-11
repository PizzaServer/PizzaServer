package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemDiamondAxe extends BaseItem implements DurableItem, ToolItem {

    public ItemDiamondAxe() {
        this(1);
    }

    public ItemDiamondAxe(int count) {
        this(count, 0);
    }

    public ItemDiamondAxe(int count, int meta) {
        super(ItemID.DIAMOND_AXE, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Axe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
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
