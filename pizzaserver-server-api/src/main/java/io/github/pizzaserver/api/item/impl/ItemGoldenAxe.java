package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemGoldenAxe extends BaseItem implements DurableItem, ToolItem {

    public ItemGoldenAxe() {
        this(1);
    }

    public ItemGoldenAxe(int meta) {
        super(ItemID.GOLDEN_AXE, 1, meta);
    }

    @Override
    public String getName() {
        return "Gold Axe";
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
        return ToolTier.GOLD;
    }

    @Override
    public int getMaxDurability() {
        return 33;
    }

}
