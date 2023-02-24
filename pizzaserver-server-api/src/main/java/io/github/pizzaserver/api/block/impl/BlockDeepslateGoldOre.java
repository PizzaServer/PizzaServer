package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateGoldOre extends BaseBlock {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_GOLD_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Gold Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
