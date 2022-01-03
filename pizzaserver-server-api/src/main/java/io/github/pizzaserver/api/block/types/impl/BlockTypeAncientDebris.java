package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeAncientDebris extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.ANCIENT_DEBRIS;
    }

    @Override
    public String getName(int blockStateIndex) {
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
