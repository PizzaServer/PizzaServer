package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.entity.Entity;

public class RedstoneOreBehavior extends DefaultBlockBehavior {

    @Override
    public void onWalkedOn(Entity entity, Block block) {
        if (block.getBlockId().equals(BlockID.DEEPSLATE_REDSTONE_ORE)) {
            block.getWorld().setAndUpdateBlock(BlockID.LIT_DEEPSLATE_REDSTONE_ORE, block.getLocation().toVector3i());
        } else if (block.getBlockId().equals(BlockID.REDSTONE_ORE)) {
            block.getWorld().setAndUpdateBlock(BlockID.LIT_REDSTONE_ORE, block.getLocation().toVector3i());
        }
    }

    @Override
    public void onUpdate(BlockUpdateType type, Block block) {
        if (type == BlockUpdateType.NEIGHBOUR) {
            if (block.getBlockId().equals(BlockID.DEEPSLATE_REDSTONE_ORE)) {
                block.getWorld().setAndUpdateBlock(BlockID.LIT_DEEPSLATE_REDSTONE_ORE, block.getLocation().toVector3i());
            } else if (block.getBlockId().equals(BlockID.REDSTONE_ORE)) {
                block.getWorld().setAndUpdateBlock(BlockID.LIT_REDSTONE_ORE, block.getLocation().toVector3i());
            }
        } else if (type == BlockUpdateType.RANDOM) {
            if (block.getBlockId().equals(BlockID.LIT_DEEPSLATE_REDSTONE_ORE)) {
                block.getWorld().setAndUpdateBlock(BlockID.DEEPSLATE_REDSTONE_ORE, block.getLocation().toVector3i());
            } else if (block.getBlockId().equals(BlockID.LIT_REDSTONE_ORE)) {
                block.getWorld().setAndUpdateBlock(BlockID.REDSTONE_ORE, block.getLocation().toVector3i());
            }
        }
    }

}
