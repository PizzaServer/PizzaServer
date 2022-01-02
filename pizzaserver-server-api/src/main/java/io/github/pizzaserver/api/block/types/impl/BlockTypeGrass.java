package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

public class BlockTypeGrass extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Grass";
    }

    @Override
    public float getHardness(int blockStateIndex) {
        return 0.5f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

}
