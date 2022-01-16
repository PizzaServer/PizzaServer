package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemShears extends Item implements ToolItemComponent, DurableItemComponent {

    public ItemShears() {
        this(1);
    }

    public ItemShears(int count) {
        this(count, 0);
    }

    public ItemShears(int count, int meta) {
        super(ItemID.SHEARS, count, meta);
    }

    @Override
    public String getName() {
        return "Shears";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SHEARS;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.NONE;
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }
}
