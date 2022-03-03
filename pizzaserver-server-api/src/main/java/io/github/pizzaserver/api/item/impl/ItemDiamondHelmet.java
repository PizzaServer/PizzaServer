package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamondHelmet extends ItemArmor {

    public ItemDiamondHelmet() {
        this(1);
    }

    public ItemDiamondHelmet(int count) {
        this(count, 0);
    }

    public ItemDiamondHelmet(int count, int meta) {
        super(ItemID.DIAMOND_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Helmet";
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
        return 363;
    }

}
