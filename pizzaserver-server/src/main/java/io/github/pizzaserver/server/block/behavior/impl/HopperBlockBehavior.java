package io.github.pizzaserver.server.block.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockHopper;
import io.github.pizzaserver.api.entity.Entity;

public class HopperBlockBehavior extends BaseBlockBehavior<BlockHopper> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockHopper block, BlockFace face, Vector3f clickPosition) {
        block.setDirection(face.opposite().toDirection());
        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

}
