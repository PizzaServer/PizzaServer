package io.github.pizzaserver.server.block.behavior.impl;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.impl.BlockPressurePlate;
import io.github.pizzaserver.api.entity.Entity;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;

public class PressurePlateBlockBehavior extends RequiresSolidBottomBlockBehavior<BlockPressurePlate> {

    @Override
    public void onCollision(Entity entity, BlockPressurePlate block) {
        if (!block.isActivated()) {
            block.setRedstoneSignal(15);
            block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
            block.getWorld().playSound(SoundEvent.POWER_ON, block.getLocation().toVector3f());
            block.getWorld().requestBlockUpdate(BlockUpdateType.BLOCK, block.getLocation().toVector3i(), 30);
        }
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockPressurePlate block) {
        if (type == BlockUpdateType.BLOCK && block.isActivated()) {
            for (Entity entity : block.getWorld().getEntitiesNear(block.getLocation().toVector3f(), 1)) {
                if (entity.getBoundingBox().collidesWith(block.getBoundingBox())) {
                    // Reschedule block update again
                    block.getWorld().requestBlockUpdate(BlockUpdateType.BLOCK, block.getLocation().toVector3i(), 30);
                    return;
                }
            }

            block.setRedstoneSignal(0);
            block.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
            block.getWorld().playSound(SoundEvent.POWER_OFF, block.getLocation().toVector3f());
        } else {
            super.onUpdate(type, block);
        }
    }

}
