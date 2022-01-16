package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.entity.Entity;

public class IceBlockBehavior extends DefaultBlockBehavior {

    @Override
    public void onBreak(Entity entity, Block block) {
        boolean hasSilkTouch = false;   // TODO: silk touch check
        if (!hasSilkTouch) {
            entity.getWorld().setAndUpdateBlock(BlockID.WATER, block.getLocation().toVector3i());
        } else {
            super.onBreak(entity, block);
        }
    }

}
