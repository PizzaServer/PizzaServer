package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.VanillaItemType;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;

public class ItemTypeStonePickaxe extends VanillaItemType implements DurableItemComponent {

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
        return ToolTypes.getToolType(ToolTypeID.STONE_PICKAXE);
    }

    @Override
    public int getMaxDurability() {
        return 130;
    }

}
