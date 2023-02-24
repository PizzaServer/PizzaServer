package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockDispenser;

public interface BlockEntityDispenser extends BlockEntityContainer<BlockDispenser> {

    String ID = "Dispenser";

    @Override
    default String getId() {
        return ID;
    }

}
