package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;

public interface BlockInventory extends Inventory {

    Block getBlock();

}
