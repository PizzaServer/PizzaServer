package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronChestplate extends ItemArmor {

    public ItemIronChestplate() {
        this(1);
    }

    public ItemIronChestplate(int count) {
        this(count, 0);
    }

    public ItemIronChestplate(int count, int meta) {
        super(ItemID.IRON_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Chestplate";
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
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 240;
    }

}
