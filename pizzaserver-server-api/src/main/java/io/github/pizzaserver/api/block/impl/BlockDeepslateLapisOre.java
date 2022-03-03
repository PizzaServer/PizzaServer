package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockDeepslateLapisOre extends BlockLapisOre {

    @Override
    public String getBlockId() {
        return BlockID.DEEPSLATE_LAPIS_ORE;
    }

    @Override
    public String getName() {
        return "Deepslate Lapis Lazuli Ore";
    }

    @Override
    public float getHardness() {
        return 4.5f;
    }

}
