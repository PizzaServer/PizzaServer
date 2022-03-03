package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;

public class BlockGoldOre extends Block {

    @Override
    public String getBlockId() {
        return BlockID.GOLD_ORE;
    }

    @Override
    public String getName() {
        return "Gold Ore";
    }

    @Override
    public float getHardness() {
        return 3;
    }

    @Override
    public float getBlastResistance() {
        return 3;
    }

}
