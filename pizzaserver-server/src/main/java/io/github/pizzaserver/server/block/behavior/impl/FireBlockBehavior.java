package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.impl.BlockFire;
import io.github.pizzaserver.api.entity.Entity;

public class FireBlockBehavior extends RequiresSolidBottomBlockBehavior<BlockFire> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockFire fire, BlockFace face, Vector3f clickPosition) {
        Block originBlock = fire.getSide(face.opposite());
        return originBlock.canBeIgnited()
                && super.prepareForPlacement(entity, fire, face, clickPosition);
    }

    @Override
    public void onCollision(Entity entity, BlockFire fire) {
        entity.setFireTicks(160);
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockFire fire) {
        Block parentBlock = fire.getSide(BlockFace.BOTTOM);
        if (!parentBlock.hasCollision()) {
            fire.getWorld().setAndUpdateBlock(BlockID.AIR, fire.getLocation().toLocation().toVector3i());
        }
    }

}
