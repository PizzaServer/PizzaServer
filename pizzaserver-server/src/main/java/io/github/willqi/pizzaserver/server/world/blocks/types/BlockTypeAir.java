package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockTypeID;

public class BlockTypeAir extends BaseBlockType {

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
