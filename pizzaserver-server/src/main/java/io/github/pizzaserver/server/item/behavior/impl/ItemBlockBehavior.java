package io.github.pizzaserver.server.item.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.trait.LiquidTrait;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.event.type.block.BlockPlaceEvent;
import io.github.pizzaserver.api.item.behavior.impl.DefaultItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Optional;

public class ItemBlockBehavior extends DefaultItemBehavior<ItemBlock> {

    @Override
    public boolean onInteract(Player player, ItemBlock itemBlock, Block block, BlockFace blockFace, Vector3f clickPosition) {
        // Replaceable blocks (other than liquids) should directly change the block instead of the
        // block of the face provided. (e.g. grass)
        if (!player.getAdventureSettings().canBuild()) {
            return false;
        }

        if (itemBlock.isAir()) {
            // Air has no specific behavior.
            // we should not resend the blocks when trying to place it.
            return true;
        }

        if (block.isAir()) {
            // Client is attempting to place blocks against air.
            // resend the blocks
            return false;
        }

        Block replacedBlock;
        if (!(block instanceof LiquidTrait) && block.isReplaceable()) {
            replacedBlock = block;
        } else {
            replacedBlock = block.getSide(blockFace);
        }

        Block placedBlock = itemBlock.getBlock().clone();
        placedBlock.setLocation(replacedBlock.getWorld(),
                replacedBlock.getX(),
                replacedBlock.getY(),
                replacedBlock.getZ(),
                replacedBlock.getLayer());
        if (!placedBlock.getBehavior().prepareForPlacement(player, placedBlock, blockFace, clickPosition)) {
            return false;
        }

        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(player, placedBlock, block);
        player.getServer().getEventManager().call(blockPlaceEvent);
        if (blockPlaceEvent.isCancelled()) {
            return false;
        }

        if (!player.getGamemode().equals(Gamemode.CREATIVE)) {
            itemBlock.setCount(itemBlock.getCount() - 1);
            player.getInventory().setSlot(player.getInventory().getSelectedSlot(), itemBlock);
        }

        block.getWorld().setAndUpdateBlock(placedBlock, placedBlock.getLocation().toVector3i());
        placedBlock.getBehavior().onPlace(player, placedBlock, blockFace);
        Optional<BlockEntity<? extends Block>> blockEntity = block.getWorld().getBlockEntity(block.getLocation().toVector3i());
        blockEntity.ifPresent(entity -> ((BaseBlockEntity<? extends Block>) entity).onPlace(player));
        return true;
    }

}
