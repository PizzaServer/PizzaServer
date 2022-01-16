package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemChainmailHelmet extends ItemArmor {

    public ItemChainmailHelmet() {
        this(1);
    }

    public ItemChainmailHelmet(int count) {
        this(count, 0);
    }

    public ItemChainmailHelmet(int count, int meta) {
        super(ItemID.CHAINMAIL_HELMET, count, meta);
    }

    @Override
    public String getName() {
        return "Chainmail Helmet";
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
