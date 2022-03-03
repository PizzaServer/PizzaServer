package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamondLeggings extends ItemArmor {

    public ItemDiamondLeggings() {
        this(1);
    }

    public ItemDiamondLeggings(int count) {
        this(count, 0);
    }

    public ItemDiamondLeggings(int count, int meta) {
        super(ItemID.DIAMOND_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Diamond Leggings";
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
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 495;
    }

}
