package io.github.willqi.pizzaserver.server.level.world.blocks.types;

import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;

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
