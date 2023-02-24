package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

/**
 * Block entities with an inventory.
 * @param <T> the block entity
 */
public interface BlockEntityInventory<B extends Block, T extends BlockEntity<B>> extends BlockInventory<B> {

    T getBlockEntity();

}