package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockFlowingWater extends BlockWater {

    public BlockFlowingWater() { }

    public BlockFlowingWater(int depth) {
        super(depth);
    }

    @Override
    public String getBlockId() {
        return BlockID.FLOWING_WATER;
    }

    @Override
    public String getName() {
        return "Flowing Water";
    }
}
