package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityInventory<T extends BlockEntity<? extends Block>> extends Inventory {

    T getBlockEntity();

}