package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.PushResponse;

public class BlockDeny extends Block {

    @Override
    public String getBlockId() {
        return BlockID.DENY;
    }

    @Override
    public String getName() {
        return "Deny";
    }

    @Override
    public float getHardness() {
        return -1;
    }

    @Override
    public PushResponse getPushResponse() {
        return PushResponse.DENY;
    }

}
