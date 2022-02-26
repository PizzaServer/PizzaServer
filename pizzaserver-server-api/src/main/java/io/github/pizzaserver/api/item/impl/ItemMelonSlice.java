package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemMelonSlice extends BaseItem {

    public ItemMelonSlice() {
        this(1);
    }

    public ItemMelonSlice(int count) {
        super(ItemID.MELON_SLICE, count);
    }

    @Override
    public String getName() {
        return "Melon";
    }
}
