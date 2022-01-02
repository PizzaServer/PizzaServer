package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BlockTypeID;

public class BlockTypeFlowingWater extends BlockTypeWater {

    @Override
    public String getBlockId() {
        return BlockTypeID.FLOWING_WATER;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Flowing Water";
    }

}
