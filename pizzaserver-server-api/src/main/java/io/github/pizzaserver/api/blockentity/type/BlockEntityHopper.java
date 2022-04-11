package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockHopper;

public interface BlockEntityHopper extends BlockEntityContainer<BlockHopper> {

    String ID = "Hopper";

    @Override
    default String getId() {
        return ID;
    }

}
