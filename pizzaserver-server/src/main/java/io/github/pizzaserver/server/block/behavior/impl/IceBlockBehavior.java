package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.impl.BlockIce;
import io.github.pizzaserver.api.entity.Entity;

public class IceBlockBehavior extends BaseBlockBehavior<BlockIce> {

    @Override
    public void onBreak(Entity entity, BlockIce ice) {
        boolean hasSilkTouch = false;   // TODO: silk touch check
        if (!hasSilkTouch) {
            entity.getWorld().setAndUpdateBlock(BlockID.WATER, ice.getLocation().toVector3i());
        } else {
            super.onBreak(entity, ice);
        }
    }

}
