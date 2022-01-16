package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemIronSword extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemIronSword() {
        this(1);
    }

    public ItemIronSword(int count) {
        this(count, 0);
    }

    public ItemIronSword(int count, int meta) {
        super(ItemID.IRON_SWORD, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
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
