package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemPrismarineShard extends BaseItem {

    public ItemPrismarineShard() {
        this(1);
    }

    public ItemPrismarineShard(int count) {
        super(ItemID.PRISMARINE_SHARD, count);
    }

    @Override
    public String getName() {
        return "Prismarine Shard";
    }

}
