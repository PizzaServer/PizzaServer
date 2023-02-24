package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCoal extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.COAL;
    }

    @Override
    public String getName() {
        return "Block of Coal";
    }

    @Override
    public float getHardness() {
        return 5;
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

    @Override
    public int getFuelTicks() {
        return 16000;
    }

}
