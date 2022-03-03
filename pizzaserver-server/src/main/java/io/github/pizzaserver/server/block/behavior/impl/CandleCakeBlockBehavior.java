package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockCake;
import io.github.pizzaserver.api.block.impl.BlockCandleCake;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemFlintAndSteel;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.player.Player;

public class CandleCakeBlockBehavior extends DefaultBlockBehavior<BlockCandleCake> {

    @Override
    public boolean onInteract(Player player, BlockCandleCake candleCake, BlockFace face, Vector3f clickPosition) {
        if (player.isSneaking()) {
            return true;
        }

        if (player.getInventory().getHeldItem() instanceof ItemFlintAndSteel && !candleCake.isLit()) {
            candleCake.setLit(true);
            candleCake.getWorld().setAndUpdateBlock(candleCake, candleCake.getLocation().toVector3i());
            return false;
        }

        if (player.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
            // Drop the candle
            for (Item item : candleCake.getDrops(player)) {
                candleCake.getWorld().addItemEntity(item, candleCake.getLocation().toVector3f(), EntityItem.getRandomMotion());
            }

            // Replace block with bitten cake
            BlockCake bittenCakeBlock = new BlockCake();
            bittenCakeBlock.setBites(1);
            candleCake.getWorld().setAndUpdateBlock(bittenCakeBlock, candleCake.getLocation().toVector3i());
            return false;
        }

        // TODO: restore hunger once hunger gets implemented

        return true;
    }

}
