package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateRedstoneOre extends BlockRedstoneOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_REDSTONE_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Redstone Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
