package io.github.pizzaserver.server.block.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.impl.BlockButton;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.player.Player;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;

public class ButtonBlockBehavior extends BaseBlockBehavior<BlockButton> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockButton button, BlockFace face, Vector3f clickPosition) {
        button.setBlockState(face.ordinal());
        return button.getSide(face.opposite()).hasCollision();
    }

    @Override
    public boolean onInteract(Player player, BlockButton button, BlockFace face, Vector3f clickPosition) {
        if (button.getBlockState() < 6) {
            button.setBlockState(button.getBlockState() + 6);   // Power it on
            button.getWorld().setAndUpdateBlock(button, button.getLocation().toVector3i());
            button.getWorld().playSound(SoundEvent.POWER_ON, button.getLocation().toVector3f());
            button.getWorld().requestBlockUpdate(BlockUpdateType.BLOCK, button.getLocation().toVector3i(), 30);
        }
        return true;
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockButton button) {
        switch (type) {
            case BLOCK:
                if (button.getBlockState() >= 6) {
                    button.setBlockState(button.getBlockState() - 6);   // power it off
                    button.getWorld().setAndUpdateBlock(button, button.getLocation().toVector3i());
                    button.getWorld().playSound(SoundEvent.POWER_OFF, button.getLocation().toVector3f());
                }
                break;
            case NEIGHBOUR:
                Block parentBlock = button.getSide(BlockFace.resolve(button.getBlockState()).opposite());
                if (!parentBlock.hasCollision()) {
                    button.getWorld().addItemEntity(button.toItem(),
                            button.getLocation().toVector3f(),
                            EntityItem.getRandomMotion());
                    button.getWorld().setAndUpdateBlock(BlockID.AIR, button.getLocation().toLocation().toVector3i());
                }
                break;
        }
    }

}
