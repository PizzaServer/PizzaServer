package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldenBoots extends ItemArmor {

    public ItemGoldenBoots() {
        this(1);
    }

    public ItemGoldenBoots(int count) {
        this(count, 0);
    }

    public ItemGoldenBoots(int count, int meta) {
        super(ItemID.GOLDEN_BOOTS, count, meta);
    }

    @Override
    public String getName() {
        return "Golden Boots";
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
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 91;
    }
}
