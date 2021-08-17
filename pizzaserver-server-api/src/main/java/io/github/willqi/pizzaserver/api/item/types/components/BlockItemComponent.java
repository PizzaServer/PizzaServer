package io.github.willqi.pizzaserver.api.item.types.components;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;

/**
 * Represents an item that is a block
 */
public interface BlockItemComponent {

    /**
     * Get the specific block that is to be placed when this item is used
     * @return block to be placed
     */
    Block getBlock();

}
