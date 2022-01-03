package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.Entity;

public class BlockTypeBarrier extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.BARRIER;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Barrier";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

}
