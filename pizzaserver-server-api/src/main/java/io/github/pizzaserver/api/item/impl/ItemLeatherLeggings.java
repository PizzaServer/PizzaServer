package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLeatherLeggings extends ItemArmor {

    public ItemLeatherLeggings() {
        this(1);
    }

    public ItemLeatherLeggings(int count) {
        this(count, 0);
    }

    public ItemLeatherLeggings(int count, int meta) {
        super(ItemID.LEATHER_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Leather Leggings";
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
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 75;
    }
}
