package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGhastTear extends BaseItem {

    public ItemGhastTear() {
        this(1);
    }

    public ItemGhastTear(int count) {
        super(ItemID.GHAST_TEAR, count);
    }

    @Override
    public String getName() {
        return "Ghast Tear";
    }

}
