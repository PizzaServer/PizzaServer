package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCryingObsidian extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CRYING_OBSIDIAN;
    }

    @Override
    public String getName() {
        return "Crying Obsidian";
    }

    @Override
    public float getHardness() {
        return 50;
    }

    @Override
    public float getBlastResistance() {
        return 1200;
    }

    @Override
    public int getLightEmission() {
        return 10;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.DIAMOND;
    }
}
