package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockMobSpawner extends Block {

    @Override
    public String getBlockId() {
        return BlockID.MOB_SPAWNER;
    }

    @Override
    public String getName() {
        return "Mob Spawner";
    }

    @Override
    public float getBlastResistance() {
        return 5;
    }

    @Override
    public float getHardness() {
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
