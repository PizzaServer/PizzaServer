package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldenHelmet extends ItemArmor {

    public ItemGoldenHelmet() {
        this(1);
    }

    public ItemGoldenHelmet(int count) {
        this(count, 0);
    }

    public ItemGoldenHelmet(int count, int meta) {
        super(ItemID.GOLDEN_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Helmet";
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
        return 77;
    }

}
