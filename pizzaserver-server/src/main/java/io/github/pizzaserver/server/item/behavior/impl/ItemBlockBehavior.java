package io.github.pizzaserver.server.item.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.descriptors.BlockEntityContainer;
import io.github.pizzaserver.api.block.descriptors.Liquid;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.event.type.block.BlockPlaceEvent;
import io.github.pizzaserver.api.item.behavior.impl.DefaultItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;

import java.util.Set;

public class ItemBlockBehavior extends DefaultItemBehavior<ItemBlock> {

    @Override
    public boolean onInteract(Player player, ItemBlock itemBlock, Block block, BlockFace blockFace) {
        // Replaceable blocks (other than liquids) should directly change the block instead of the
        // block of the face provided. (e.g. grass)
        Block blockAtPlacementPos;
        if (!(block instanceof Liquid) && block.isReplaceable()) {
            blockAtPlacementPos = block;
        } else {
            blockAtPlacementPos = block.getSide(blockFace);
        }

        if (!block.isAir() && blockAtPlacementPos.isReplaceable()) {
            if (!player.getAdventureSettings().canBuild()) {
                return false;
            }
            Block placedBlock = itemBlock.getBlock();
            placedBlock.setLocation(blockAtPlacementPos.getWorld(),
                                    blockAtPlacementPos.getX(),
                                    blockAtPlacementPos.getY(),
                                    blockAtPlacementPos.getZ(),
                                    (block instanceof Liquid) ? 1 : 0);
            if (!placedBlock.getBehavior().prepareForPlacement(player, placedBlock, blockFace)) {
                return false;
            }

            if (placedBlock.hasCollision()) {
                // Collision check with nearby entities
                Set<Entity> nearByEntities = block.getLocation()
                                                  .getWorld()
                                                  .getEntitiesNear(block.getLocation().toVector3f(), 16);
                for (Entity entity : nearByEntities) {
                    boolean entityCollidesWithBlock =
                            placedBlock.getBoundingBox().collidesWith(entity.getBoundingBox()) && entity.hasCollision()
                                    && !(entity instanceof EntityItem) && (entity.getViewers().contains(player)
                                    || entity.equals(player));

                    if (entityCollidesWithBlock) {
                        return false;
                    }
                }
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
            if (block instanceof BlockEntityContainer<? extends BlockEntity> blockWithBlockEntity) {
                if (blockWithBlockEntity.getBlockEntity() != null) {
                    blockWithBlockEntity.getBlockEntity().onPlace(player);
                }
            }
            return true;
        } else {
            // air blocks don't change the world at all and cannot really be placed.
            // but for all other blocks return false since the block should not have
            // been placed clientside.
            return itemBlock.isEmpty();
        }
    }
}
