package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BellAttachmentType;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.entity.Entity;

public class BellBlockBehavior extends DefaultBlockBehavior<BlockBell> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockBell block, BlockFace face, Vector3f clickPosition) {
        if (!super.prepareForPlacement(entity, block, face, clickPosition)) {
            return false;
        }

        switch (face) {
            case BOTTOM -> block.setAttachmentType(BellAttachmentType.HANGING);
            case TOP -> block.setAttachmentType(BellAttachmentType.STANDING);
            case NORTH, SOUTH, EAST, WEST -> block.setAttachmentType(BellAttachmentType.SIDE);
        }

        block.setDirection(entity.getHorizontalDirection().opposite());

        if (block.getSide(face.opposite()).isTransparent()) {
            return false;
        }

        if (!block.getLocation().getBlock().isReplaceable()) {
            return false;
        }

        return true;
    }

}
