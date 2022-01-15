package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockPodzol extends Block {

    @Override
    public String getBlockId() {
        return BlockID.PODZOL;
    }

    @Override
    public String getName() {
        return "Podzol";
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

}
