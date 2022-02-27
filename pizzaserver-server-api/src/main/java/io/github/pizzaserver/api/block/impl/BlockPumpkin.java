package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockPumpkin extends Block {

    @Override
    public String getBlockId(){ return BlockID.PUMPKIN; }

    @Override
    public String getName(){ return "Pumpkin"; }

    @Override
    public float getHardness(){ return 1;}

    @Override
    public float getBlastResistance() {
        return 1;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }
}
