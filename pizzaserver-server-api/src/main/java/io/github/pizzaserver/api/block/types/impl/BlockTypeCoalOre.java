package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeCoalOre extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.COAL_ORE;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Coal Ore";
    }

    @Override
    public float getHardness() {
        return 3f;
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
