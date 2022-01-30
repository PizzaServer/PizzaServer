package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGlisteringMelonSlice extends BaseItem {

    public ItemGlisteringMelonSlice() {
        this(1);
    }

    public ItemGlisteringMelonSlice(int count) {
        super(ItemID.GLISTERING_MELON_SLICE, count);
    }

    @Override
    public String getName() {
        return "Glistering Melon Slice";
    }

}
