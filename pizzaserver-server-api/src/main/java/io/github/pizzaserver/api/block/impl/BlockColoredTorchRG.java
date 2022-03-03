package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

public class BlockColoredTorchRG extends Block {

    @Override
    public String getBlockId() {
        return BlockID.COLORED_TORCH_RG;
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public float getHardness() {
        return 0;
    }

}
