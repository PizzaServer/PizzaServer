package io.github.pizzaserver.api.item.types;

import io.github.pizzaserver.api.block.types.BlockType;

public interface BlockItemType extends ItemType {

    /**
     * Get the block type this item represents.
     * @return block type
     */
    BlockType getBlockType();

}
