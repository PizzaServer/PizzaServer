package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronHelmet extends ItemArmor {

    public ItemIronHelmet() {
        this(1);
    }

    public ItemIronHelmet(int count) {
        this(count, 0);
    }

    public ItemIronHelmet(int count, int meta) {
        super(ItemID.IRON_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Helmet";
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
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 165;
    }

}
