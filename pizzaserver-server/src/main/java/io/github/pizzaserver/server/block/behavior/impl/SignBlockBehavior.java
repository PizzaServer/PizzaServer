package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.data.StandingSignDirection;
import io.github.pizzaserver.api.block.impl.BlockSign;
import io.github.pizzaserver.api.block.impl.BlockStandingSign;
import io.github.pizzaserver.api.block.impl.BlockWallSign;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.api.player.Player;

public class SignBlockBehavior extends BaseBlockBehavior<BlockSign> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockSign block, BlockFace face, Vector3f clickPosition) {
        // set direction
        if (block instanceof BlockWallSign wallSign) {
            wallSign.setDirection(face.toDirection().toHorizontal());
        } else {
            BlockStandingSign standingSign = (BlockStandingSign) block;
            standingSign.setDirection(StandingSignDirection.fromYaw(entity.get(EntityKeys.ROTATION_YAW).orElse(0f)));
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public void onPlace(Entity entity, BlockSign block, BlockFace face) {
        if (entity instanceof Player player) {
            // when a sign block is placed, the sign can only be edited by that player.
            block.getBlockEntity().setEditor(player);
        }
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockSign block) {
        boolean dropSign;
        if (block instanceof BlockWallSign wallSign) {
            // Check if the block the sign attached to is air
            dropSign = block.getSide(wallSign.getDirection().opposite().toFace()).isAir();
        } else {
            // Check if the block below it is air
            dropSign = block.getSide(BlockFace.BOTTOM).isAir();
        }

        if (dropSign) {
            block.getWorld().addItemEntity(block.toItem(), block.getLocation().toVector3f(), EntityItem.getRandomMotion());
            block.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toLocation().toVector3i());
        }
    }

}
