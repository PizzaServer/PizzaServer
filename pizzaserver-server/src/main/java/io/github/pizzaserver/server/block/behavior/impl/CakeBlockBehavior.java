package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.*;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.player.Player;

public class CakeBlockBehavior extends BaseBlockBehavior<BlockCake> {

    @Override
    public boolean onInteract(Player player, BlockCake cake, BlockFace face, Vector3f clickPosition) {
        if (player.isSneaking()) {
            return true;
        }

        // Handle case where the player wants to place a candle on the block.
        if (player.getInventory().getHeldItem() instanceof ItemBlock
                && ((ItemBlock) player.getInventory().getHeldItem()).getBlock() instanceof BlockCandle candleBlock
                && cake.getBites() == 0) {

            Block candleCakeBlock;
            if (candleBlock instanceof BlockColoredCandle coloredCandle) {
                candleCakeBlock = new BlockColoredCandle(coloredCandle.getColor());
            } else {
                candleCakeBlock = new BlockCandleCake();
            }

            cake.getWorld().setAndUpdateBlock(candleCakeBlock, cake.getLocation().toVector3i());
            return false;
        }

        if (player.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
            if (cake.getBites() == 6) {
                player.getWorld().setAndUpdateBlock(new BlockAir(), cake.getLocation().toVector3i());
            } else {
                cake.setBites(cake.getBites() + 1);
                player.getWorld().setAndUpdateBlock(cake, cake.getLocation().toVector3i());
            }
            return false;
        }

        // TODO: restore hunger once hunger gets implemented

        return true;
    }

}
