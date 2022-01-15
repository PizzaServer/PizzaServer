package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLeatherChestplate extends ItemArmor {

    public ItemLeatherChestplate() {
        this(1);
    }

    public ItemLeatherChestplate(int count) {
        this(count, 0);
    }

    public ItemLeatherChestplate(int count, int meta) {
        super(ItemID.LEATHER_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Leather Chestplate";
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
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 80;
    }

}
