package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemGlowInkSac extends Item {

    public ItemGlowInkSac() {
        this(1);
    }

    public ItemGlowInkSac(int count) {
        super(ItemID.GLOW_INK_SAC, count);
    }

    @Override
    public String getName() {
        return "Glow Ink Sac";
    }
}
