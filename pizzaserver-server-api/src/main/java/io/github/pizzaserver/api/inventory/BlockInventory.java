package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;

/**
 * Blocks which have an inventory.
 * @param <T> the block the inventory belongs to
 */
public interface BlockInventory<T extends Block> extends Inventory {

    T getBlock();

}
