package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteLeggings extends ItemArmor {

    public ItemNetheriteLeggings() {
        this(1);
    }

    public ItemNetheriteLeggings(int count) {
        this(count, 0);
    }

    public ItemNetheriteLeggings(int count, int meta) {
        super(ItemID.NETHERITE_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Leggings";
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
        return 555;
    }

}
