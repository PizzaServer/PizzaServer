package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCandle;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemFlintAndSteel;
import io.github.pizzaserver.api.player.Player;

public class CandleBlockBehavior extends RequiresSolidBottomBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face, Vector3f clickPosition) {
        BlockCandle candle = (BlockCandle) block;

        // If the player clicks the candle block
        Block baseBlock = block.getSide(face.opposite());
        boolean isDirectlyAddingCandleToBlock = baseBlock instanceof BlockCandle baseCandle
                && baseCandle.getBlockId().equals(block.getBlockId())
                && baseCandle.getCandleCount() != 4;

        if (isDirectlyAddingCandleToBlock) {
            candle.setLocation(baseBlock.getLocation());
            switch (((BlockCandle) baseBlock).getCandleCount()) {
                case 1 -> candle.setCandleCount(2);
                case 2 -> candle.setCandleCount(3);
                case 3 -> candle.setCandleCount(4);
            }

            return true;
        }

        // If the player clicks the block under the candle
        Block replacedBlock = block.getWorld().getBlock(block.getLocation().toVector3i());
        boolean isDirectingAddingCandleToParentBlock = replacedBlock instanceof BlockCandle replacedCandle
                && replacedCandle.getBlockId().equals(block.getBlockId())
                && replacedCandle.getCandleCount() != 4;

        if (isDirectingAddingCandleToParentBlock) {
            switch (((BlockCandle) replacedBlock).getCandleCount()) {
                case 1 -> candle.setCandleCount(2);
                case 2 -> candle.setCandleCount(3);
                case 3 -> candle.setCandleCount(4);
            }

            return true;
        }

        return super.prepareForPlacement(entity, block, face, clickPosition);
    }

    @Override
    public boolean onInteract(Player player, Block block, BlockFace face, Vector3f clickPosition) {
        Item itemInHand = player.getInventory().getHeldItem();
        if (itemInHand instanceof ItemFlintAndSteel) {
            if (((BlockCandle) block).isLit()) {
                return false;
            }

            ((BlockCandle) block).setLit(true);
            player.getWorld().setAndUpdateBlock(block, block.getLocation().toVector3i());
        }
        return true;
    }

}
