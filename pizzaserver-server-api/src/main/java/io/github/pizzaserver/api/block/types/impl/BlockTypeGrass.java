package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.item.ToolTypeRegistry;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockTypeGrass extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Grass";
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 0.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

}
