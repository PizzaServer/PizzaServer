package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemEnchantedBook extends BaseItem {

    public ItemEnchantedBook() {
        this(1);
    }

    public ItemEnchantedBook(int count) {
        super(ItemID.ENCHANTED_BOOK, count);
    }

    @Override
    public String getName() {
        return "Enchanted Book";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
