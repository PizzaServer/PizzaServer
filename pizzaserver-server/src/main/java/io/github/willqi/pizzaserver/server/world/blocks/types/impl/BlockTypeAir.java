package io.github.willqi.pizzaserver.server.world.blocks.types.impl;

import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeID;

public class BlockTypeAir extends BlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.AIR;
    }

    @Override
    public String getName() {
        return "Air";
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
