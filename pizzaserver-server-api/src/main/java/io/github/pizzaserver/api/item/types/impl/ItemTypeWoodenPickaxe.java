package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;

public class ItemTypeWoodenPickaxe extends BaseItemType implements DurableItemComponent {

    @Override
    public String getItemId() {
        return ItemTypeID.WOODEN_PICKAXE;
    }

    @Override
    public String getName() {
        return "Wooden Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.WOOD;
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

}
