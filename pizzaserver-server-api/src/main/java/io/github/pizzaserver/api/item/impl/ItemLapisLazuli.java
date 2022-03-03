package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemLapisLazuli extends BaseItem {

    public ItemLapisLazuli() {
        this(1);
    }

    public ItemLapisLazuli(int count) {
        super(ItemID.LAPIS_LAZULI, count);
    }

    @Override
    public String getName() {
        return "Lapis Lazuli";
    }

}
