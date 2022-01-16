package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemGoldenSword extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemGoldenSword() {
        this(1);
    }

    public ItemGoldenSword(int count) {
        this(count, 0);
    }

    public ItemGoldenSword(int count, int meta) {
        super(ItemID.GOLDEN_SWORD, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
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
