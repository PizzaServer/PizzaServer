package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCrimsonNylium extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CRIMSON_NYLIUM;
    }

    @Override
    public String getName() {
        return "Crimson Nylium";
    }

    @Override
    public float getHardness() {
        return 0.4f;
    }

    @Override
    public float getBlastResistance() {
        return 1;
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
