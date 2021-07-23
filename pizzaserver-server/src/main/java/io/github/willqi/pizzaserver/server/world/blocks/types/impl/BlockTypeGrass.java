package io.github.willqi.pizzaserver.server.world.blocks.types.impl;

import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeID;

public class BlockTypeGrass extends BlockType {

    @Override
    public Identifier getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

}
