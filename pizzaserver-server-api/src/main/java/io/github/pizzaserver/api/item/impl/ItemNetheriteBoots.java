package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNetheriteBoots extends ItemArmor {

    public ItemNetheriteBoots() {
        this(1);
    }

    public ItemNetheriteBoots(int count) {
        this(count, 0);
    }

    public ItemNetheriteBoots(int count, int meta) {
        super(ItemID.NETHERITE_BOOTS, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Boots";
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
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 481;
    }
}
