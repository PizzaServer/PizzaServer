package io.github.pizzaserver.api.item.types;

import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.event.type.block.BlockPlaceEvent;
import io.github.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;

/**
 * Any block ItemStack is an instance of this class to prevent the need to create thousands of item classes for each block.
 */
public class BaseBlockItemType extends BaseItemType implements BlockItemType {

    private final BaseBlockType blockType;


    public BaseBlockItemType(BaseBlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public BaseBlockType getBlockType() {
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
    public String getIconName() {
        throw new UnsupportedOperationException("Unable to retrieve icon URL for a BlockItemType");
    }

    @Override
    public boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        // Handle placing a block
        // TODO: Account for entity collision
        Block blockAtPlacementPos = block.getSide(blockFace);
        if (!block.getBlockState().isAir() && (blockAtPlacementPos.getBlockState().isLiquid() || !blockAtPlacementPos.getBlockState().isSolid())) {
            if (!player.getAdventureSettings().canBuild()) {
                return false;
            }
            Block placedBlock = this.getBlockType().create(itemStack.getMeta());
            placedBlock.setLocation(block.getWorld(), block.getSide(blockFace).getLocation().toVector3i());

            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(player, placedBlock, block);
            player.getServer().getEventManager().call(blockPlaceEvent);
            if (blockPlaceEvent.isCancelled()) {
                return false;
            }

            if (!player.getGamemode().equals(Gamemode.CREATIVE)) {
                itemStack.setCount(itemStack.getCount() - 1);
                player.getInventory().setSlot(player.getInventory().getSelectedSlot(), itemStack);
            }
            block.getWorld().setBlock(placedBlock, placedBlock.getLocation().toVector3i());
            return true;
        } else {
            // air blocks don't change the world at all and cannot really be placed.
            // but for all other blocks return false since the block should not have
            // been placed clientside.
            return itemStack.isEmpty();
        }
    }

}
