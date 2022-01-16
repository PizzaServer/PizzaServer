package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteHelmet extends ItemArmor {

    public ItemNetheriteHelmet() {
        this(1);
    }

    public ItemNetheriteHelmet(int count) {
        this(count, 0);
    }

    public ItemNetheriteHelmet(int count, int meta) {
        super(ItemID.NETHERITE_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Helmet";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.HELMET;
    }

    @Override
    public int getProtection() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 407;
    }
}
