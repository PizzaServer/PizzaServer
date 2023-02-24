package io.github.pizzaserver.server.item.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.IgniteCause;
import io.github.pizzaserver.api.block.impl.BlockFire;
import io.github.pizzaserver.api.event.type.block.BlockIgniteEvent;
import io.github.pizzaserver.api.item.behavior.impl.BaseItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemFireCharge;
import io.github.pizzaserver.api.player.Player;

public class ItemFireChargeBehavior extends BaseItemBehavior<ItemFireCharge> {

    @Override
    public boolean onInteract(Player player, ItemFireCharge fireCharge, Block block, BlockFace blockFace, Vector3f clickPosition) {
        Block replacedBlock = block.getSide(blockFace);
        if (!replacedBlock.isAir()) {
            return false;
        }

        if (replacedBlock.getSide(BlockFace.BOTTOM).hasCollision()) {
            BlockIgniteEvent igniteEvent = new BlockIgniteEvent(replacedBlock, IgniteCause.FIREBALL, player);
            player.getServer().getEventManager().call(igniteEvent);
            if (igniteEvent.isCancelled()) {
                return false;
            }

            block.getSide(blockFace).getWorld().setAndUpdateBlock(new BlockFire(), replacedBlock.getLocation().toVector3i());
        }

        fireCharge.setCount(fireCharge.getCount() - 1);
        player.getInventory().setHeldItem(fireCharge);
        return true;
    }

}
