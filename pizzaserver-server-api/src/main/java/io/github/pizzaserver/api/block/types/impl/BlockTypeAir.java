package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;

public class BlockTypeAir extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.AIR;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Air";
    }

    @Override
    public boolean isSolid(int blockStateIndex) {
        return false;
    }

    @Override
    public float getToughness(int blockStateIndex) {
        return 0;
    }

}
