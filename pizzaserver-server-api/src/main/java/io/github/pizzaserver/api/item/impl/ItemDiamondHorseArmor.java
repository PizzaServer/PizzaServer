package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ItemID;

public class ItemDiamondHorseArmor extends ItemHorseArmor {

    public ItemDiamondHorseArmor() {
        super(ItemID.DIAMOND_HORSE_ARMOR);
    }

    @Override
    public String getName() {
        return "Diamond Horse Armor";
    }

}
