package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemWoodenHoe extends BaseItem implements DurableItem, ToolItem {

    public ItemWoodenHoe() {
        this(1);
    }

    public ItemWoodenHoe(int meta) {
        super(ItemID.WOODEN_HOE, 1, meta);
    }

    @Override
    public String getName() {
        return "Wooden Hoe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.HOE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.WOOD;
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

}
