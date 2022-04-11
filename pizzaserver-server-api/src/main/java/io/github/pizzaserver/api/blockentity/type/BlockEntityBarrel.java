package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockBarrel;

public interface BlockEntityBarrel extends BlockEntityContainer<BlockBarrel> {

    String ID = "Barrel";

    @Override
    default String getId() {
        return ID;
    }

}
