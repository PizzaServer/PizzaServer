package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockAncientDebris extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.ANCIENT_DEBRIS;
    }

    @Override
    public String getName() {
        return "Ancient Debris";
    }

    @Override
    public float getHardness() {
        return 30;
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
