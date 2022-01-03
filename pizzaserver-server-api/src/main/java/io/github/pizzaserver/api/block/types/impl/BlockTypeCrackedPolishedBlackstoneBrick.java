package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeCrackedPolishedBlackstoneBrick extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.CRACKED_POLISHED_BLACKSTONE_BRICK;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Cracked Polished Blackstone Bricks";
    }

    @Override
    public float getHardness() {
        return 1.5f;
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
