package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockGravel extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.GRAVEL;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public float getHardness() {
        return 0.6f;
    }

    @Override
    public float getBlastResistance() {
        return 0.6f;
    }

    @Override
    public boolean isAffectedByGravity() {
        return true;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
