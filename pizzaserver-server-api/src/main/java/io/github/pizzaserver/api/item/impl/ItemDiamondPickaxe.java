package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemDiamondPickaxe extends BaseItem implements DurableItem, ToolItem {

    public ItemDiamondPickaxe() {
        this(1);
    }

    public ItemDiamondPickaxe(int count) {
        this(count, 0);
    }

    public ItemDiamondPickaxe(int count, int meta) {
        super(ItemID.DIAMOND_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Pickaxe";
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
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.DIAMOND;
    }

    @Override
    public int getMaxDurability() {
        return 1561;
    }

}
