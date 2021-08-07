package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockTypeID;

public class BlockTypeGrass extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.GRASS;
    }

    @Override
    public String getName() {
        return "Grass";
    }

}
