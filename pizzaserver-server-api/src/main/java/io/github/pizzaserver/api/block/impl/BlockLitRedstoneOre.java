package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockLitRedstoneOre extends BlockRedstoneOre {

    @Override
    public String getBlockId() {
        return BlockID.LIT_REDSTONE_ORE;
    }

    @Override
    public String getName() {
        return "Lit Redstone Ore";
    }

}
