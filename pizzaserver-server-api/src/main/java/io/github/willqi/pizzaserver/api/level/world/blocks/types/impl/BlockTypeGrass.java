package io.github.willqi.pizzaserver.api.level.world.blocks.types.impl;

import io.github.willqi.pizzaserver.api.item.ToolTypeRegistry;
import io.github.willqi.pizzaserver.api.item.data.ToolType;
import io.github.willqi.pizzaserver.api.item.data.ToolTypeID;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockTypeGrass extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public float getToughness() {
        return 0.5f;
    }

    @Override
    public Set<ToolType> getCorrectTools() {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

    @Override
    public Set<ToolType> getBestTools() {
        return new HashSet<>(Arrays.asList(ToolTypeRegistry.getToolType(ToolTypeID.NONE), ToolTypeRegistry.getToolType(ToolTypeID.WOOD_SHOVEL)));
    }

}
