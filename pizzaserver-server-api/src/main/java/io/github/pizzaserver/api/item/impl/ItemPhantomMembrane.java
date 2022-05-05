package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemPhantomMembrane extends BaseItem {

    public ItemPhantomMembrane() {
        this(1);
    }

    public ItemPhantomMembrane(int count) {
        super(ItemID.PHANTOM_MEMBRANE, count);
    }

    @Override
    public String getName() {
        return "Phantom Membrane";
    }

}
