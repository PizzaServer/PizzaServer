package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;

public class ItemTypeWoodenSword extends BaseItemType implements DurableItemComponent {

    @Override
    public String getItemId() {
        return ItemTypeID.WOODEN_SWORD;
    }

    @Override
    public String getName() {
        return "Wooden Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
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
