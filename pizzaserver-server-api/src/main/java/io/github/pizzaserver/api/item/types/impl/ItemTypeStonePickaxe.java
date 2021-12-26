package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;

public class ItemTypeStonePickaxe extends BaseItemType implements DurableItemComponent {

    @Override
    public String getItemId() {
        return ItemTypeID.STONE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Stone Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.STONE;
    }

    @Override
    public int getMaxDurability() {
        return 130;
    }

}
