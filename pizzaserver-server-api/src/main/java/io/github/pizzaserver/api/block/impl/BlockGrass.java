package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockGrass extends Block {

    @Override
    public String getBlockId() {
        return BlockID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
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
