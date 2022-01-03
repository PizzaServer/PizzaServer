package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeClay extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.CLAY;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Clay Block";
    }

    @Override
    public float getHardness() {
        return 0.6f;
    }

    @Override
    public float getBlastResistance() {
        return 0.6f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
