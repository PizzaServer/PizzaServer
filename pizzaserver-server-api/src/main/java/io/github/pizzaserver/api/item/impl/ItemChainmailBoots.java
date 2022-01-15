package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemChainmailBoots extends ItemArmor {

    public ItemChainmailBoots() {
        this(1);
    }

    public ItemChainmailBoots(int count) {
        this(count, 0);
    }

    public ItemChainmailBoots(int count, int meta) {
        super(ItemID.CHAINMAIL_BOOTS, count, meta);
    }

    @Override
    public String getName() {
        return "Chainmail Boots";
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
        return 195;
    }

}
