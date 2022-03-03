package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemArrow extends BaseItem {

    public ItemArrow() {
        this(1);
    }

    public ItemArrow(int count) {
        super(ItemID.ARROW, count);
    }

    @Override
    public String getName() {
        return "Arrow";
    }

    @Override
    public boolean isAllowedInOffHand() {
        return true;
    }

    @Override
    public boolean isStackedByMeta() {
        return true;
    }

}
