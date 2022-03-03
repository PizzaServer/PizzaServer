package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemPaper extends BaseItem {

    public ItemPaper() {
        this(1);
    }

    public ItemPaper(int count) {
        super(ItemID.PAPER, count);
    }

    @Override
    public String getName() {
        return "Paper";
    }

}
