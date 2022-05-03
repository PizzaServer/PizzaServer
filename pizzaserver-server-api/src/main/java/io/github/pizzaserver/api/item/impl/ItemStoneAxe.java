package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemStoneAxe extends BaseItem implements DurableItem, ToolItem {

    public ItemStoneAxe() {
        this(1);
    }

    public ItemStoneAxe(int meta) {
        super(ItemID.STONE_AXE, 1, meta);
    }

    @Override
    public String getName() {
        return "Stone Axe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.AXE;
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
