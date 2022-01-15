package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemRabbitHide extends Item {

    public ItemRabbitHide() {
        this(1);
    }

    public ItemRabbitHide(int count) {
        super(ItemID.RABBIT_HIDE, count);
    }

    @Override
    public String getName() {
        return "Rabbit Hide";
    }

}
