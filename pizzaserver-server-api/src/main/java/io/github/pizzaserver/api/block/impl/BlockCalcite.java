package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockCalcite extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.CALCITE;
    }

    @Override
    public String getName() {
        return "Calcite";
    }

    @Override
    public float getHardness() {
        return 0.75f;
    }

    @Override
    public float getBlastResistance() {
        return 0.75f;
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
