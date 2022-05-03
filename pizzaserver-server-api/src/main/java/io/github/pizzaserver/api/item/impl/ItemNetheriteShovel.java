package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemNetheriteShovel extends BaseItem implements DurableItem, ToolItem {

    public ItemNetheriteShovel() {
        this(1);
    }

    public ItemNetheriteShovel(int meta) {
        super(ItemID.NETHERITE_SHOVEL, 1, meta);
    }

    @Override
    public String getName() {
        return "Netherite Shovel";
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
        return ToolTier.NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 2032;
    }

}
