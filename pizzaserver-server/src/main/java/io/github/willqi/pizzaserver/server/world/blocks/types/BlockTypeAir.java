package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.server.world.blocks.BlockType;

public class BlockTypeAir extends BlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.AIR;
    }

    @Override
    public String getName() {
        return "Air";
    }

}
