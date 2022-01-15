package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockClay extends Block {

    @Override
    public String getBlockId() {
        return BlockID.CLAY;
    }

    @Override
    public String getName() {
        return "Clay Block";
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
