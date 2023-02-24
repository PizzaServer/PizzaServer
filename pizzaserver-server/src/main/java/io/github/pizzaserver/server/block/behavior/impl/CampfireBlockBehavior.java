package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.entity.Entity;

public class CampfireBlockBehavior extends HorizontalBlockBehavior<BlockCampfire> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockCampfire block, BlockFace face, Vector3f clickPosition) {
        Block parentBlock = block.getSide(face.opposite());
        if (!parentBlock.hasCollision() || parentBlock instanceof BlockCampfire) {
            return false;
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

}
