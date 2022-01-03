package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeCopperOre extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.COPPER_ORE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Copper Ore";
    }

    @Override
    public float getHardness() {
        return 3f;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.STONE;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

}
