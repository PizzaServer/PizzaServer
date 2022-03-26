package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;

public interface BlockInventory<T extends Block> extends Inventory {

    T getBlock();

}
