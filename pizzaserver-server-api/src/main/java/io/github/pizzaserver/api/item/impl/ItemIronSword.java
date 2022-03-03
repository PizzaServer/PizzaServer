package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemIronSword extends BaseItem implements DurableItem, ToolItem {

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
