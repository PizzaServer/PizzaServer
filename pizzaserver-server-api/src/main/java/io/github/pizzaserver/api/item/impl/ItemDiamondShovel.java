package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemDiamondShovel extends BaseItem implements DurableItem, ToolItem {

    public ItemDiamondShovel() {
        this(1);
    }

    public ItemDiamondShovel(int meta) {
        super(ItemID.DIAMOND_SHOVEL, 1, meta);
    }

    @Override
    public String getName() {
        return "Diamond Shovel";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SHOVEL;
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
