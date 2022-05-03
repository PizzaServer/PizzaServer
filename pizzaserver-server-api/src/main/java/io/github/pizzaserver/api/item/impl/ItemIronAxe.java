package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemIronAxe extends BaseItem implements DurableItem, ToolItem {

    public ItemIronAxe() {
        this(1);
    }

    public ItemIronAxe(int meta) {
        super(ItemID.IRON_AXE, 1, meta);
    }

    @Override
    public String getName() {
        return "Iron Axe";
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
        return ToolTier.IRON;
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

}
