package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemChainmailChestplate extends ItemArmor {

    public ItemChainmailChestplate() {
        this(1);
    }

    public ItemChainmailChestplate(int count) {
        this(count, 0);
    }

    public ItemChainmailChestplate(int count, int meta) {
        super(ItemID.CHAINMAIL_CHESTPLATE, count, meta);
    }

    @Override
    public String getName() {
        return "Chainmail Chestplate";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ArmorSlot getArmorSlot() {
        return ArmorSlot.CHESTPLATE;
    }

    @Override
    public int getProtection() {
        return 5;
    }

    @Override
    public int getMaxDurability() {
        return 240;
    }

}
