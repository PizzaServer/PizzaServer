package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemChainmailLeggings extends ItemArmor {

    public ItemChainmailLeggings() {
        this(1);
    }

    public ItemChainmailLeggings(int count) {
        this(count, 0);
    }

    public ItemChainmailLeggings(int count, int meta) {
        super(ItemID.CHAINMAIL_LEGGINGS, count, meta);
    }

    @Override
    public String getName() {
        return "Chainmail Leggings";
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
        return 4;
    }

    @Override
    public int getMaxDurability() {
        return 225;
    }

}
