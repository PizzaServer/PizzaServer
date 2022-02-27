package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityInventory extends Inventory, OpenableInventory {

    BlockEntity getBlockEntity();

}
