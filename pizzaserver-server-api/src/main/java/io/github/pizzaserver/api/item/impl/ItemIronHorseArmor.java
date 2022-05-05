package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ItemID;

public class ItemIronHorseArmor extends ItemHorseArmor {

    public ItemIronHorseArmor() {
        super(ItemID.IRON_HORSE_ARMOR);
    }

    @Override
    public String getName() {
        return "Iron Horse Armor";
    }

}
