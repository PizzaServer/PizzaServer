package io.github.pizzaserver.api.item.types;

import io.github.pizzaserver.api.block.Block;

public interface BlockItemType extends ItemType {

    /**
     * Get the block this item represents.
     * @return block
     */
    Block getBlock();

}
