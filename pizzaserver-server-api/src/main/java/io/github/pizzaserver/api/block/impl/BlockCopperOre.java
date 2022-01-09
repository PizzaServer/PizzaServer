package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCopperOre extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.COPPER_ORE;
    }

    @Override
    public String getName() {
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
