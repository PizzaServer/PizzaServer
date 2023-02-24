package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockBarrel;
import io.github.pizzaserver.api.blockentity.trait.BlockEntityOpenableTrait;

public interface BlockEntityBarrel extends BlockEntityContainer<BlockBarrel>, BlockEntityOpenableTrait {

    String ID = "Barrel";

    @Override
    default String getId() {
        return ID;
    }

}
