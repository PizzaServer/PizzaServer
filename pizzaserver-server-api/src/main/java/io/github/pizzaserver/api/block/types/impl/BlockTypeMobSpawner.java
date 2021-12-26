package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

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
    public float getHardness(int blockStateIndex) {
        return 5;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

}
