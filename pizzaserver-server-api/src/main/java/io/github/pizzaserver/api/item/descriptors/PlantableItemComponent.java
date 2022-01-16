package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.block.Block;

/**
 * Represents an item type that can place down a block when used
 * e.g. crops
 */
public interface PlantableItemComponent {

    /**
     * Get the block to be placed when this item is used.
     * @return block to be placed
     */
    Block getPlacedBlock();

    /**
     * Get the block types this item can be used on.
     * @return block types this item can be used on
     */
    Block[] getPlaceableBlockTypes();
}
