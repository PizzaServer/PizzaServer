package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGoldNugget extends BaseItem {

    public ItemGoldNugget() {
        this(1);
    }

    public ItemGoldNugget(int count) {
        super(ItemID.GOLD_NUGGET, count);
    }

    @Override
    public String getName() {
        return "Gold Nugget";
    }

}
