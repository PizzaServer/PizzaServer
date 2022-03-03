package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronBoots extends ItemArmor {

    public ItemIronBoots() {
        this(1);
    }

    public ItemIronBoots(int count) {
        this(count, 0);
    }

    public ItemIronBoots(int count, int meta) {
        super(ItemID.IRON_BOOTS, count, meta);
    }

    @Override
    public String getName() {
        return "Iron Boots";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.BOOTS;
    }

    @Override
    public int getProtection() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 195;
    }

}
