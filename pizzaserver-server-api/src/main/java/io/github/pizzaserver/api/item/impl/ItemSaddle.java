package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemSaddle extends BaseItem {

    public ItemSaddle() {
        this(1);
    }

    public ItemSaddle(int count) {
        super(ItemID.SADDLE, count);
    }

    @Override
    public String getName() {
        return "Saddle";
    }

}
