package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemRabbitFoot extends BaseItem {

    public ItemRabbitFoot() {
        this(1);
    }

    public ItemRabbitFoot(int count) {
        super(ItemID.RABBIT_FOOT, count);
    }

    @Override
    public String getName() {
        return "Rabbit Foot";
    }

}
