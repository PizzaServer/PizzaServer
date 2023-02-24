package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockEnderChest;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityEnderChest extends BlockEntity<BlockEnderChest> {

    String ID = "EnderChest";

    @Override
    default String getId() {
        return ID;
    }

}
