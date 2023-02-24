package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemFireCharge extends BaseItem {

    public ItemFireCharge() {
        this(1);
    }

    public ItemFireCharge(int count) {
        super(ItemID.FIRE_CHARGE, count);
    }

    @Override
    public String getName() {
        return "Fire Charge";
    }

}
