package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamondBoots extends ItemArmor {

    public ItemDiamondBoots() {
        this(1);
    }

    public ItemDiamondBoots(int count) {
        this(count, 0);
    }

    public ItemDiamondBoots(int count, int meta) {
        super(ItemID.DIAMOND_BOOTS, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Boots";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.BOOTS;
    }

    @Override
    public int getProtection() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 429;
    }
}
