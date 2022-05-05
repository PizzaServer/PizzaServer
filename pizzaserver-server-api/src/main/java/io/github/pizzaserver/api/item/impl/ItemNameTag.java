package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemNameTag extends BaseItem {

    public ItemNameTag() {
        this(1);
    }

    public ItemNameTag(int count) {
        super(ItemID.NAME_TAG, count);
    }

    @Override
    public String getName() {
        return "Name Tag";
    }

}
