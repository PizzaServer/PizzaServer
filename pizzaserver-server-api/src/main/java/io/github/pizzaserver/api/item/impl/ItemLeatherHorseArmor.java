package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLeatherHorseArmor extends ItemHorseArmor {

    public ItemLeatherHorseArmor() {
        super(ItemID.LEATHER_HORSE_ARMOR);
    }

    @Override
    public String getName() {
        return "Leather Horse Armor";
    }

}
