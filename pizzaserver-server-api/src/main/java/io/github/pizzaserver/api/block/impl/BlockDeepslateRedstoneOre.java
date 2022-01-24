package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;

public class BlockDeepslateRedstoneOre extends BlockRedstoneOre {

    public BlockDeepslateRedstoneOre() {
        super();
    }

    public BlockDeepslateRedstoneOre(LitType litType) {
        super(litType);
    }

    @Override
    public String getBlockId() {
        if (this.isLit()) {
            return BlockID.LIT_DEEPSLATE_REDSTONE_ORE;
        } else {
            return BlockID.DEEPSLATE_REDSTONE_ORE;
        }
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
