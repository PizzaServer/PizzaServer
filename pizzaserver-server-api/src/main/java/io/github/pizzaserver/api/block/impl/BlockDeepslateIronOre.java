package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateIronOre extends BlockIronOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_IRON_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Iron Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
