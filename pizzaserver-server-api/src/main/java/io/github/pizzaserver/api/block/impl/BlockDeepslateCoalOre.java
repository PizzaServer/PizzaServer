package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateCoalOre extends BlockCoalOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_COAL_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Coal Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
