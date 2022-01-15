package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldenChestplate extends ItemArmor {

    public ItemGoldenChestplate() {
        this(1);
    }

    public ItemGoldenChestplate(int count) {
        this(count, 0);
    }

    public ItemGoldenChestplate(int count, int meta) {
        super(ItemID.GOLDEN_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Chestplate";
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
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 112;
    }

}
