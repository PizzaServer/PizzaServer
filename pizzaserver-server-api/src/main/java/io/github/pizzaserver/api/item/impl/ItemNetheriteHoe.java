package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemNetheriteHoe extends BaseItem implements DurableItem, ToolItem {

    public ItemNetheriteHoe() {
        this(1);
    }

    public ItemNetheriteHoe(int meta) {
        super(ItemID.NETHERITE_HOE, 1, meta);
    }

    @Override
    public String getName() {
        return "Netherite Hoe";
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
        return ToolTier.NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 2032;
    }

}
