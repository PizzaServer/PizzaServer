package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemIronHoe extends BaseItem implements DurableItem, ToolItem {

    public ItemIronHoe() {
        this(1);
    }

    public ItemIronHoe(int meta) {
        super(ItemID.DIAMOND_HOE, 1, meta);
    }

    @Override
    public String getName() {
        return "Iron Hoe";
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
        return ToolTier.IRON;
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

}
