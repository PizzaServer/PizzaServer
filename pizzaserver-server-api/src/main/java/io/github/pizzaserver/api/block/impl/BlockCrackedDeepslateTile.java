package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCrackedDeepslateTile extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CRACKED_DEEPSLATE_TILE;
    }

    @Override
    public String getName() {
        return "Cracked Deepslate Tiles";
    }

    @Override
    public float getHardness() {
        return 3.5f;
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
