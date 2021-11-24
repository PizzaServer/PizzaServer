package io.github.pizzaserver.api.level.world.blocks.types.impl;

import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

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
