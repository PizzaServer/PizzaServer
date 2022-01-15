package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemFermentedSpiderEye extends Item {

    public ItemFermentedSpiderEye() {
        this(1);
    }

    public ItemFermentedSpiderEye(int count) {
        super(ItemID.FERMENTED_SPIDER_EYE, count);
    }

    @Override
    public String getName() {
        return "Fermented Spider Eye";
    }

}
