package io.github.willqi.pizzaserver.server.world.blocks.types.impl;

import io.github.willqi.pizzaserver.server.world.blocks.types.BedrockBlockType;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockTypeID;

public class BlockTypeGrass extends BedrockBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

}
