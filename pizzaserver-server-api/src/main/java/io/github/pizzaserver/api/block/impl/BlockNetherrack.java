package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockNetherrack extends Block {

    @Override
    public String getBlockId() {
        return BlockID.NETHERRACK;
    }

    @Override
    public String getName() {
        return "Netherrack";
    }

    @Override
    public float getHardness() {
        return 0.4f;
    }

    @Override
    public float getBlastResistance() {
        return 0.4f;
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
