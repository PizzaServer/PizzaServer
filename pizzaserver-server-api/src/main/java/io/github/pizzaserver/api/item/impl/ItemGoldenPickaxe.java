package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemGoldenPickaxe extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemGoldenPickaxe() {
        this(1);
    }

    public ItemGoldenPickaxe(int count) {
        this(count, 0);
    }

    public ItemGoldenPickaxe(int count, int meta) {
        super(ItemID.GOLDEN_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.GOLD;
    }

    @Override
    public int getMaxDurability() {
        return 32;
    }

}
