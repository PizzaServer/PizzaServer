package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockTypeCalcite extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.CALCITE;
    }

    @Override
    public String getName(int blockStateIndex) {
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
