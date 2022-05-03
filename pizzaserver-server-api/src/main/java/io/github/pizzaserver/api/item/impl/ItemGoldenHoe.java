package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemGoldenHoe extends BaseItem implements DurableItem, ToolItem {

    public ItemGoldenHoe() {
        this(1);
    }

    public ItemGoldenHoe(int meta) {
        super(ItemID.GOLDEN_HOE, 1, meta);
    }

    @Override
    public String getName() {
        return "Gold Hoe";
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
        return ToolTier.GOLD;
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

}
