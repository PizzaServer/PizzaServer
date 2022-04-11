package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockDropper;

public interface BlockEntityDropper extends BlockEntityContainer<BlockDropper> {

    String ID = "Dropper";

    @Override
    default String getId() {
        return ID;
    }

}
