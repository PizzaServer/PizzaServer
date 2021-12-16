package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;

import java.util.Collections;
import java.util.Set;

public class BlockTypeIronOre extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.IRON_ORE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Iron Ore";
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 3f;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.STONE_PICKAXE));
    }

}
