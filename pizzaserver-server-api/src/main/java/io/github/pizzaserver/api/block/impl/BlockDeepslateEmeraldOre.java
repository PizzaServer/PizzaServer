package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateEmeraldOre extends BlockEmeraldOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_EMERALD_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Emerald Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
