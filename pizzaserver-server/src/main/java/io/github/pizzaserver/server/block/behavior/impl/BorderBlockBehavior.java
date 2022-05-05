package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.data.BorderAttachmentType;
import io.github.pizzaserver.api.block.impl.BlockBorder;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.utils.HorizontalDirection;

public class BorderBlockBehavior extends BaseBlockBehavior<BlockBorder> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockBorder block, BlockFace face, Vector3f clickPosition) {
        if (block.getSide(BlockFace.NORTH).hasCollision()) {
            block.setAttachmentType(HorizontalDirection.NORTH, BorderAttachmentType.TALL);
            block.setWallPost(true);
        }
        if (block.getSide(BlockFace.SOUTH).hasCollision()) {
            block.setAttachmentType(HorizontalDirection.SOUTH, BorderAttachmentType.SHORT);
        }
        if (block.getSide(BlockFace.EAST).hasCollision()) {
            block.setAttachmentType(HorizontalDirection.EAST, BorderAttachmentType.TALL);
            block.setWallPost(true);
        }
        if (block.getSide(BlockFace.WEST).hasCollision()) {
            block.setAttachmentType(HorizontalDirection.WEST, BorderAttachmentType.SHORT);
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockBorder block) {
        BorderAttachmentType northAttachmentType = block.getAttachmentType(HorizontalDirection.NORTH);
        BorderAttachmentType southAttachmentType = block.getAttachmentType(HorizontalDirection.SOUTH);
        BorderAttachmentType eastAttachmentType = block.getAttachmentType(HorizontalDirection.EAST);
        BorderAttachmentType westAttachmentType = block.getAttachmentType(HorizontalDirection.WEST);

        block.setAttachmentType(HorizontalDirection.NORTH, block.getSide(BlockFace.NORTH).hasCollision() ? BorderAttachmentType.TALL : BorderAttachmentType.NONE);
        block.setAttachmentType(HorizontalDirection.SOUTH, block.getSide(BlockFace.SOUTH).hasCollision() ? BorderAttachmentType.SHORT : BorderAttachmentType.NONE);
        block.setAttachmentType(HorizontalDirection.EAST, block.getSide(BlockFace.EAST).hasCollision() ? BorderAttachmentType.TALL : BorderAttachmentType.NONE);
        block.setAttachmentType(HorizontalDirection.WEST, block.getSide(BlockFace.WEST).hasCollision() ? BorderAttachmentType.SHORT : BorderAttachmentType.NONE);

        boolean updated = (northAttachmentType != block.getAttachmentType(HorizontalDirection.NORTH))
                || (southAttachmentType != block.getAttachmentType(HorizontalDirection.SOUTH))
                || (eastAttachmentType != block.getAttachmentType(HorizontalDirection.EAST))
                || (westAttachmentType != block.getAttachmentType(HorizontalDirection.WEST));

        if (updated) {
            block.getWorld().setAndUpdateBlock(block, block.getX(), block.getY(), block.getZ(), block.getLayer());
        }
    }
}
