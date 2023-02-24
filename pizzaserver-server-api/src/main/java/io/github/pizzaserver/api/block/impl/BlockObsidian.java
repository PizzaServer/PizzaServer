package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockObsidian extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.OBSIDIAN;
    }

    @Override
    public String getName() {
        return "Obsidian";
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
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.DIAMOND;
    }

}
