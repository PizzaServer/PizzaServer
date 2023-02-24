package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBowl extends BaseItem {

    public ItemBowl() {
        this(1);
    }

    public ItemBowl(int count) {
        super(ItemID.BOWL, count);
    }

    @Override
    public String getName() {
        return "Bowl";
    }

    @Override
    public int getFuelTicks() {
        return 200;
    }

}
