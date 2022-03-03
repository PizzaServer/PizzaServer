package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronLeggings extends ItemArmor {

    public ItemIronLeggings() {
        this(1);
    }

    public ItemIronLeggings(int count) {
        this(count, 0);
    }

    public ItemIronLeggings(int count, int meta) {
        super(ItemID.IRON_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Leggings";
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
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 225;
    }

}
