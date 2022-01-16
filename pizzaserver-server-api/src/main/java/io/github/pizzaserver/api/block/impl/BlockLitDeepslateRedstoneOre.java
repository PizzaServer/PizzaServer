package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockLitDeepslateRedstoneOre extends BlockDeepslateRedstoneOre {

    @Override
    public String getBlockId() {
        return BlockID.LIT_DEEPSLATE_REDSTONE_ORE;
    }

    @Override
    public String getName() {
        return "Lit Deepslate Redstone Ore";
    }

}
