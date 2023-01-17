package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCandle;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemFlintAndSteel;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.api.player.Player;

public class CandleBlockBehavior extends RequiresSolidBottomBlockBehavior<BlockCandle> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockCandle candle, BlockFace face, Vector3f clickPosition) {
        // If the player clicks the candle block
        Block baseBlock = candle.getSide(face.opposite());
        boolean isDirectlyAddingCandleToBlock = baseBlock instanceof BlockCandle baseCandle
                && baseCandle.getBlockId().equals(candle.getBlockId())
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
        Block replacedBlock = candle.getWorld().getBlock(candle.getLocation().toVector3i());
        boolean isDirectingAddingCandleToParentBlock = replacedBlock instanceof BlockCandle replacedCandle
                && replacedCandle.getBlockId().equals(candle.getBlockId())
                && replacedCandle.getCandleCount() != 4;

        if (isDirectingAddingCandleToParentBlock) {
            switch (((BlockCandle) replacedBlock).getCandleCount()) {
                case 1 -> candle.setCandleCount(2);
                case 2 -> candle.setCandleCount(3);
                case 3 -> candle.setCandleCount(4);
            }

            return true;
        }

        return super.prepareForPlacement(entity, candle, face, clickPosition);
    }

    @Override
    public boolean onInteract(Player player, BlockCandle candle, BlockFace face, Vector3f clickPosition) {
        Item itemInHand = player.getInventory().getHeldItem();
        if (itemInHand instanceof ItemFlintAndSteel) {
            if (candle.isLit()) {
                return false;
            }

            candle.setLit(true);
            player.expect(EntityKeys.WORLD).setAndUpdateBlock(candle, candle.getLocation().toVector3i());
        }
        return true;
    }

}
