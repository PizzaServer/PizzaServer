package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemCompass extends BaseItem {

    public ItemCompass() {
        this(1);
    }

    public ItemCompass(int count) {
        super(ItemID.COMPASS, count);
    }

    @Override
    public String getName() {
        return "Compass";
    }

}
