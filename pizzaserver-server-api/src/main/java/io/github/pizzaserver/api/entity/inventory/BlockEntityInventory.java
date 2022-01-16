package io.github.pizzaserver.api.entity.inventory;

import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityInventory extends Inventory {

    BlockEntity getBlockEntity();
}
