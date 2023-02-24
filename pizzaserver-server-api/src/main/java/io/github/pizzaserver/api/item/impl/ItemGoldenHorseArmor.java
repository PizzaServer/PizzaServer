package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldenHorseArmor extends ItemHorseArmor {

    public ItemGoldenHorseArmor() {
        super(ItemID.GOLDEN_HORSE_ARMOR);
    }

    @Override
    public String getName() {
        return "Golden Horse Armor";
    }

}
