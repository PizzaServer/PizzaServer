package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemIronPickaxe extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemIronPickaxe() {
        this(1);
    }

    public ItemIronPickaxe(int count) {
        this(count, 0);
    }

    public ItemIronPickaxe(int count, int meta) {
        super(ItemID.IRON_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.IRON;
    }

    @Override
    public int getMaxDurability() {
        return 250;
    }
}
