package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockChest;

public interface BlockEntityChest extends BlockEntityContainer<BlockChest> {

    String ID = "Chest";

    @Override
    default String getId() {
        return ID;
    }

}
