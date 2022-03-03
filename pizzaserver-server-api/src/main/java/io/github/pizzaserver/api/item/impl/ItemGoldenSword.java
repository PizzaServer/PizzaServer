package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.item.descriptors.ToolItem;

public class ItemGoldenSword extends BaseItem implements DurableItem, ToolItem {

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
