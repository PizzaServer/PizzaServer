package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.DurableItem;

public class ItemFlintAndSteel extends BaseItem implements DurableItem {

    public ItemFlintAndSteel() {
        this(1);
    }

    public ItemFlintAndSteel(int count) {
        super(ItemID.FLINT_AND_STEEL, count);
    }

    @Override
    public String getName() {
        return "Flint and Steel";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 65;
    }

}
