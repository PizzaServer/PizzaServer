package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.PushResponse;

public class BlockBarrier extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.BARRIER;
    }

    @Override
    public String getName() {
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

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.DENY;
    }

}
