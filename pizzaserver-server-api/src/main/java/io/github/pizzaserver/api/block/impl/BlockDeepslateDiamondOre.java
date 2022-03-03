package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateDiamondOre extends BlockDiamondOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_DIAMOND_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Diamond Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
