package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemAmethystShard extends BaseItem {

    public ItemAmethystShard() {
        this(1);
    }

    public ItemAmethystShard(int count) {
        super(ItemID.AMETHYST_SHARD, count);
    }

    @Override
    public String getName() {
        return "Amethyst Shard";
    }

}
