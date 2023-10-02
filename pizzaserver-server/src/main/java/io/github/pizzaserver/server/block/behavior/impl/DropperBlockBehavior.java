package io.github.pizzaserver.server.block.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockDropper;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.utils.Direction;

public class DropperBlockBehavior extends BaseBlockBehavior<BlockDropper> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockDropper block, BlockFace face, Vector3f clickPosition) {
        boolean tooFarToConsiderUpDown = Math.abs(entity.getX() - block.getX()) > 2
                && Math.abs(entity.getZ() - block.getZ()) > 2;

        float entityY = entity.getY() + entity.getEyeHeight();

        if (entityY - block.getY() > 2 && !tooFarToConsiderUpDown) {
            block.setDirection(Direction.UP);
        } else if (block.getY() - entityY > 0 && !tooFarToConsiderUpDown) {
            block.setDirection(Direction.DOWN);
        } else {
            block.setDirection(entity.getHorizontalDirection().toDirection().opposite());
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

}