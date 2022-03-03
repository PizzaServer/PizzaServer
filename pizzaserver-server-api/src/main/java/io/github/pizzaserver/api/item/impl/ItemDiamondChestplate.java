package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamondChestplate extends ItemArmor {

    public ItemDiamondChestplate() {
        this(1);
    }

    public ItemDiamondChestplate(int count) {
        this(count, 0);
    }

    public ItemDiamondChestplate(int count, int meta) {
        super(ItemID.DIAMOND_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Chestplate";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.CHESTPLATE;
    }

    @Override
    public int getProtection() {
        return 8;
    }

    @Override
    public int getMaxDurability() {
        return 528;
    }

}
