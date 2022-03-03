package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

public class BlockReserved6 extends Block {

    @Override
    public String getBlockId() {
        return BlockID.RESERVED6;
    }

    @Override
    public String getName() {
        return "Reserved6";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public float getBlastResistance() {
        return 0;
    }

}
