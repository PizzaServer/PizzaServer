package io.github.pizzaserver.server.block.behavior.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.impl.BlockRedstoneOre;
import io.github.pizzaserver.api.entity.Entity;

public class RedstoneOreBehavior extends BaseBlockBehavior<BlockRedstoneOre> {

    @Override
    public void onWalkedOn(Entity entity, BlockRedstoneOre redstoneOre) {
        if (redstoneOre.getBlockId().equals(BlockID.DEEPSLATE_REDSTONE_ORE)) {
            redstoneOre.getWorld().setAndUpdateBlock(BlockID.LIT_DEEPSLATE_REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
        } else if (redstoneOre.getBlockId().equals(BlockID.REDSTONE_ORE)) {
            redstoneOre.getWorld().setAndUpdateBlock(BlockID.LIT_REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
        }
    }

    @Override
    public void onUpdate(BlockUpdateType type, BlockRedstoneOre redstoneOre) {
        if (type == BlockUpdateType.NEIGHBOUR) {
            if (redstoneOre.getBlockId().equals(BlockID.DEEPSLATE_REDSTONE_ORE)) {
                redstoneOre.getWorld().setAndUpdateBlock(BlockID.LIT_DEEPSLATE_REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
            } else if (redstoneOre.getBlockId().equals(BlockID.REDSTONE_ORE)) {
                redstoneOre.getWorld().setAndUpdateBlock(BlockID.LIT_REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
            }
        } else if (type == BlockUpdateType.RANDOM) {
            if (redstoneOre.getBlockId().equals(BlockID.LIT_DEEPSLATE_REDSTONE_ORE)) {
                redstoneOre.getWorld().setAndUpdateBlock(BlockID.DEEPSLATE_REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
            } else if (redstoneOre.getBlockId().equals(BlockID.LIT_REDSTONE_ORE)) {
                redstoneOre.getWorld().setAndUpdateBlock(BlockID.REDSTONE_ORE, redstoneOre.getLocation().toVector3i());
            }
        }
    }

}
