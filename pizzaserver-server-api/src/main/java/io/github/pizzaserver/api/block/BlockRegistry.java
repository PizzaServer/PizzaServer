package io.github.pizzaserver.api.block;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.item.behavior.ItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBlock;

import java.util.Set;

/**
 * Contains every single block retrievable via the API.
 */
public interface BlockRegistry {

    default void register(Block block) {
        this.register(block, new BaseBlockBehavior<>());
    }

    <T extends Block> void register(T block, BlockBehavior<T> behavior);

    <T extends Block> void register(T block, BlockBehavior<T> behavior, ItemBehavior<ItemBlock> itemBehavior);

    /**
     * Check if a block id was registered.
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    boolean hasBlock(String blockId);

    /**
     * Retrieve all non-Vanilla blocks that were registered.
     * @return registered non-Vanilla blocks
     */
    Set<Block> getCustomBlocks();

    Block getBlock(String blockId);

    Block getBlock(String blockId, int state);

    default BlockBehavior<? extends Block> getBlockBehavior(String blockId) {
        return this.getBlockBehavior(this.getBlock(blockId));
    }

    <T extends Block> BlockBehavior<T> getBlockBehavior(T block);

    static BlockRegistry getInstance() {
        return Server.getInstance().getBlockRegistry();
    }

}
