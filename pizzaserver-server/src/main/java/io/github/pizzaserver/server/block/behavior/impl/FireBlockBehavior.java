package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;

public class FireBlockBehavior extends RequiresSolidBottomBlockBehavior {

    @Override
    public void onStandingOn(Entity entity, Block block) {
        entity.setFireTicks(160);
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        Block parentBlock = block.getSide(BlockFace.BOTTOM);
        if (!parentBlock.hasCollision()) {
            block.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toLocation().toVector3i());
        }
    }

}
