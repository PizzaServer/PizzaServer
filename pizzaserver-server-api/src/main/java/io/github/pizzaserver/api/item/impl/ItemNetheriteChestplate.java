package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteChestplate extends ItemArmor {

    public ItemNetheriteChestplate() {
        this(1);
    }

    public ItemNetheriteChestplate(int count) {
        this(count, 0);
    }

    public ItemNetheriteChestplate(int count, int meta) {
        super(ItemID.NETHERITE_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Chestplate";
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
        return 592;
    }

}
