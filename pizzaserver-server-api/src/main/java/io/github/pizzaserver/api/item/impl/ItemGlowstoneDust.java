package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGlowstoneDust extends Item {

    public ItemGlowstoneDust() {
        this(1);
    }

    public ItemGlowstoneDust(int count) {
        super(ItemID.GLOWSTONE_DUST, count);
    }

    @Override
    public String getName() {
        return "Glowstone Dust";
    }

}
