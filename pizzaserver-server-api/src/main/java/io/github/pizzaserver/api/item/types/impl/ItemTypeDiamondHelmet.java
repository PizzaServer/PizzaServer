package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.types.ItemTypeID;

public class ItemTypeDiamondHelmet extends ItemTypeArmor {

    @Override
    public String getItemId() {
        return ItemTypeID.DIAMOND_HELMET;
    }

    @Override
    public String getName() {
        return "Diamond Helmet";
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
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 363;
    }

}
