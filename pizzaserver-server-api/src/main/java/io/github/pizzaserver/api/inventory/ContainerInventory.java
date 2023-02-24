package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface ContainerInventory<B extends Block> extends BlockEntityInventory<B, BlockEntity<B>> {

}
