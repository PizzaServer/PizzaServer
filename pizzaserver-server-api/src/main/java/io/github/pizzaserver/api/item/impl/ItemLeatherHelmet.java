package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLeatherHelmet extends ItemArmor {

    public ItemLeatherHelmet() {
        this(1);
    }

    public ItemLeatherHelmet(int count) {
        this(count, 0);
    }

    public ItemLeatherHelmet(int count, int meta) {
        super(ItemID.LEATHER_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Leather Helmet";
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
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 55;
    }
}
