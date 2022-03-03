package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldenLeggings extends ItemArmor {

    public ItemGoldenLeggings() {
        this(1);
    }

    public ItemGoldenLeggings(int count) {
        this(count, 0);
    }

    public ItemGoldenLeggings(int count, int meta) {
        super(ItemID.GOLDEN_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Leggings";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.LEGGINGS;
    }

    @Override
    public int getProtection() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 105;
    }

}
