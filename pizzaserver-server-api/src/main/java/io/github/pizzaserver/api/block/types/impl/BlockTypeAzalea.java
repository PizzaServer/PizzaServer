package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.BlockUpdateType;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;

public class BlockTypeAzalea extends BaseBlockType {

    @Override
    public String getBlockId() {
        return BlockTypeID.AZALEA;
    }

    @Override
    public String getName(int blockStateIndex) {
        return "Azalea";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        return block.getSide(BlockFace.BOTTOM).getBlockState().hasCollision();
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        Block parentBlock = block.getSide(BlockFace.BOTTOM);
        if (!parentBlock.getBlockState().hasCollision()) {
            block.getWorld().addItemEntity(new ItemStack(this.getBlockId(), 1),
                    block.getLocation().toVector3f(),
                    ItemEntity.getRandomMotion());
            block.getWorld().setAndUpdateBlock(BlockRegistry.getInstance().getBlockType(BlockTypeID.AIR), block.getLocation().toLocation().toVector3i());
        }
    }

}
