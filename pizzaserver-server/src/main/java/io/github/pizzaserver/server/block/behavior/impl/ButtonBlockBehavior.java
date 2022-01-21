package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.player.Player;

public class ButtonBlockBehavior extends DefaultBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face, Vector3f clickPosition) {
        block.setBlockState(face.ordinal());
        return block.getSide(face.opposite()).hasCollision();
    }

    @Override
    public boolean onInteract(Player player, Block block, BlockFace face, Vector3f clickPosition) {
        if (block.getBlockState() < 6) {
            block.setBlockState(block.getBlockState() + 6);   // Power it on
            block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
            block.getWorld().playSound(SoundEvent.POWER_ON, block.getLocation().toVector3f());
            block.getWorld().requestBlockUpdate(BlockUpdateType.BLOCK, block.getLocation().toVector3i(), 30);
        }
        return true;
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        switch (type) {
            case BLOCK:
                if (block.getBlockState() >= 6) {
                    block.setBlockState(block.getBlockState() - 6);   // power it off
                    block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
                    block.getWorld().playSound(SoundEvent.POWER_OFF, block.getLocation().toVector3f());
                }
                break;
            case NEIGHBOUR:
                Block parentBlock = block.getSide(BlockFace.resolve(block.getBlockState()).opposite());
                if (!parentBlock.hasCollision()) {
                    block.getWorld().addItemEntity(block.toStack(),
                            block.getLocation().toVector3f(),
                            EntityItem.getRandomMotion());
                    block.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toLocation().toVector3i());
                }
                break;
        }
    }

}
