package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemHeartOfTheSea extends BaseItem {

    public ItemHeartOfTheSea() {
        this(1);
    }

    public ItemHeartOfTheSea(int count) {
        super(ItemID.HEART_OF_THE_SEA, count);
    }

    @Override
    public String getName() {
        return "Heart of the Sea";
    }

}
