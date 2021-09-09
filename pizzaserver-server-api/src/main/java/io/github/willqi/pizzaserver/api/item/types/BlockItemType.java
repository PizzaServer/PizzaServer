package io.github.willqi.pizzaserver.api.item.types;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Any block item class is an instance of this class to prevent the need to create thousands of item classes for each block
 */
public class BlockItemType extends BaseItemType {

    private final BaseBlockType blockType;


    public BlockItemType(BaseBlockType blockType) {
        this.blockType = blockType;
    }

    public BaseBlockType getBlockType() {
        return this.blockType;
    }

    @Override
    public String getItemId() {
        return this.blockType.getBlockId();
    }

    @Override
    public String getName() {
        return this.blockType.getName();
    }

    @Override
    public String getIconName() {
        throw new UnsupportedOperationException("Unable to retrieve icon URL for a BlockItemType");
    }

    @Override
    public boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        // Handle placing a block
        // TODO: Account for entity collision
        if (!block.isAir() && block.getSide(blockFace).isAir()) {
            itemStack.setCount(itemStack.getCount() - 1);
            player.getInventory().setSlot(player.getInventory().getSelectedSlot(), itemStack);
            block.getWorld().setBlock(this.getBlockType().create(itemStack.getDamage()), block.getSide(blockFace).getLocation());
            return true;
        } else {
            return itemStack.isEmpty(); // air blocks don't change the world at all and cannot really be placed.
                                        // but for all other blocks return false since the block should not have
                                        // been placed clientside.
        }
    }

    @Override
    public void onInteract(Player player, ItemStack itemStack, Entity entity) {}
}
