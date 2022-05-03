package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemCarrotOnAStick extends BaseItem {

    public ItemCarrotOnAStick() {
        this(1);
    }

    public ItemCarrotOnAStick(int count) {
        super(ItemID.CARROT_ON_A_STICK, count);
    }

    @Override
    public String getName() {
        return "Carrot on a Stick";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
