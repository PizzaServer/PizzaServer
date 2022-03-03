package io.github.pizzaserver.api.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.impl.ItemBlock;

public class RequiresSolidBottomBlockBehavior<T extends Block> extends DefaultBlockBehavior<T> {

    @Override
    public boolean prepareForPlacement(Entity entity, T block, BlockFace face, Vector3f clickPosition) {
        return block.getSide(BlockFace.BOTTOM).hasCollision()
                && super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public void onUpdate(BlockUpdateType type, T block) {
        Block parentBlock = block.getSide(BlockFace.BOTTOM);
        if (!parentBlock.hasCollision()) {
            block.getWorld().addItemEntity(new ItemBlock(block.getBlockId(), 1, block.getStackMeta()),
                    block.getLocation().toVector3f(),
                    EntityItem.getRandomMotion());
            block.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toLocation().toVector3i());
        }
    }

}
