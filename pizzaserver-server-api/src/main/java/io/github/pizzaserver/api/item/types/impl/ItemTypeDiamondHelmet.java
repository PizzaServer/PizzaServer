package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.data.ArmorSlot;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.components.ArmorItemComponent;

public class ItemTypeDiamondHelmet extends BaseItemType implements ArmorItemComponent {

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
