package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;

public class ItemEnderEye extends BaseItem {

    public ItemEnderEye() {
        this(1);
    }

    public ItemEnderEye(int count) {
        super(ItemID.ENDER_EYE, count);
    }

    @Override
    public String getName() {
        return "Ender Eye";
    }

}
