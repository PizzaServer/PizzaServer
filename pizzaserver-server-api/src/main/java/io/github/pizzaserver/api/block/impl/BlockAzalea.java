package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;

public class BlockAzalea extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.AZALEA;
    }

    @Override
    public String getName() {
        return "Azalea";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }
}
