package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;

public class BlockAir extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.AIR;
    }

    @Override
    public String getName() {
        return "Air";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public float getBlastResistance() {
        return -1;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }
}
