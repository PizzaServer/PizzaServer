package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBook extends BaseItem {

    public ItemBook() {
        this(1);
    }

    public ItemBook(int count) {
        super(ItemID.BOOK, count);
    }

    @Override
    public String getName() {
        return "Book";
    }

}
