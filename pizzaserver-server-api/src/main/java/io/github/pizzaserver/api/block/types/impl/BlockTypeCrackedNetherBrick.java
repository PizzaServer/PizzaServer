package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeCrackedNetherBrick extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.CRACKED_NETHER_BRICK;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Cracked Nether Bricks";
    }

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public float getBlastResistance() {
        return 6;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
