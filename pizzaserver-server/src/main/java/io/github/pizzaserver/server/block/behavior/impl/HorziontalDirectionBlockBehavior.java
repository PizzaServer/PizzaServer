package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;

public class HorziontalDirectionBlockBehavior extends DefaultBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        block.setBlockState(entity.getHorizontalDirection().opposite().getBlockStateIndex());
        return true;
    }

}
