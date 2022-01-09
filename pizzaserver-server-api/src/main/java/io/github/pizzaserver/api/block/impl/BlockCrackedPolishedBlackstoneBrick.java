package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCrackedPolishedBlackstoneBrick extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CRACKED_POLISHED_BLACKSTONE_BRICK;
    }

    @Override
    public String getName() {
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
