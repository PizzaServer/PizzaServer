package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;

import java.util.Collections;
import java.util.Set;

public class BlockTypeMobSpawner extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.MOB_SPAWNER;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Mob Spawner";
    }

    @Override
    public float getBlastResistance(int blockStateIndex) {
        return 5;
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 5;
    }

    @Override
    public Set<ToolType> getCorrectTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

    @Override
    public Set<ToolType> getBestTools(int blockStateIndex) {
        return Collections.singleton(ToolTypes.getToolType(ToolTypeID.WOOD_PICKAXE));
    }

}
