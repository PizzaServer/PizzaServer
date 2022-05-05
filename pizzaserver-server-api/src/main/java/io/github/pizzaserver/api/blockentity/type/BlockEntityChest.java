package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.trait.BlockEntityOpenableTrait;

public interface BlockEntityChest extends BlockEntityContainer<BlockChest>, BlockEntityOpenableTrait {

    String ID = "Chest";

    @Override
    default String getId() {
        return ID;
    }

}
