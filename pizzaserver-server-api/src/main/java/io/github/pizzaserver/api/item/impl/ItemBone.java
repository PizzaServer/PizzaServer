package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemBone extends Item {

    public ItemBone() {
        this(1);
    }

    public ItemBone(int count) {
        super(ItemID.BONE, count);
    }

    @Override
    public String getName() {
        return "Bone";
    }
}
