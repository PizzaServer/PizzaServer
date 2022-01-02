package io.github.pizzaserver.server.item.type;

import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.event.type.block.BlockPlaceEvent;
import io.github.pizzaserver.api.block.BlockFace;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.BlockItemType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Set;

/**
 * Any block ItemStack is an instance of this class to prevent the need to create thousands of item classes for each block.
 */
public class ImplBlockItemType extends BaseItemType implements BlockItemType {

    private final BlockType blockType;


    public ImplBlockItemType(BlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public BlockType getBlockType() {
        return this.blockType;
    }

    @Override
    public String getItemId() {
        return this.blockType.getBlockId();
    }

    @Override
    public String getName() {
        return this.getBlockType().getName(0);
    }

    @Override
    public boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        // Handle placing a block
        Block blockAtPlacementPos = block.getSide(blockFace);
        if (!block.getBlockState().isAir() && (blockAtPlacementPos.getBlockState().isLiquid() || !blockAtPlacementPos.getBlockState().isSolid())) {
            if (!player.getAdventureSettings().canBuild()) {
                return false;
            }
            Block placedBlock = this.getBlockType().create(itemStack.getMeta());
            placedBlock.setLocation(new BlockLocation(block.getWorld(), block.getSide(blockFace).getLocation().toVector3i(), block.getLayer()));
            if (!placedBlock.getBlockType().prepareForPlacement(player, placedBlock)) {
                return false;
            }

            // Collision check with nearby entities
            Set<Entity> nearByEntities = block.getLocation().getWorld().getEntitiesNear(block.getLocation().toVector3f(), 16);
            for (Entity entity : nearByEntities) {
                boolean entityCollidesWithBlock = placedBlock.getBoundingBox().collidesWith(entity.getBoundingBox())
                        && entity.hasCollision()
                        && !(entity instanceof ItemEntity)
                        && (entity.getViewers().contains(player) || entity.equals(player));

                if (entityCollidesWithBlock) {
                    return false;
                }
            }

            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(player, placedBlock, block);
            player.getServer().getEventManager().call(blockPlaceEvent);
            if (blockPlaceEvent.isCancelled()) {
                return false;
            }

            if (!player.getGamemode().equals(Gamemode.CREATIVE)) {
                itemStack.setCount(itemStack.getCount() - 1);
                player.getInventory().setSlot(player.getInventory().getSelectedSlot(), itemStack);
            }
            block.getWorld().setAndUpdateBlock(placedBlock, placedBlock.getLocation().toVector3i());
            placedBlock.getBlockType().onPlace(player, placedBlock);
            if (block.getBlockEntity() != null) {
                block.getBlockEntity().onPlace(player);
            }
            return true;
        } else {
            // air blocks don't change the world at all and cannot really be placed.
            // but for all other blocks return false since the block should not have
            // been placed clientside.
            return itemStack.isEmpty();
        }
    }

}
