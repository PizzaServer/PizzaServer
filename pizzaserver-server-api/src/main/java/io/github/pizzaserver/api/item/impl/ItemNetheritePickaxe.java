package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemNetheritePickaxe extends BaseItem implements DurableItem, ToolItem {

    public ItemNetheritePickaxe() {
        this(1);
    }

    public ItemNetheritePickaxe(int count) {
        this(count, 0);
    }

    public ItemNetheritePickaxe(int count, int meta) {
        super(ItemID.NETHERITE_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 2031;
    }

}
