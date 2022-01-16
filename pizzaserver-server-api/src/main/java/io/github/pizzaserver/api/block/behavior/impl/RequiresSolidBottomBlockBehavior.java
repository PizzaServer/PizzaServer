package io.github.pizzaserver.api.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.impl.ItemBlock;

public class RequiresSolidBottomBlockBehavior extends DefaultBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        return block.getSide(BlockFace.BOTTOM).hasCollision();
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        Block parentBlock = block.getSide(BlockFace.BOTTOM);
        if (!parentBlock.hasCollision()) {
            block.getWorld()
                 .addItemEntity(new ItemBlock(block.getBlockId(), 1),
                                block.getLocation().toVector3f(),
                                EntityItem.getRandomMotion());
            block.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toLocation().toVector3i());
        }
    }
}
