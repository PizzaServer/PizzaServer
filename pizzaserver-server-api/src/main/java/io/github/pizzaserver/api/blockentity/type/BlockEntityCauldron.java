package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockCauldron;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityCauldron extends BlockEntity<BlockCauldron> {

    String ID = "Cauldron";

    @Override
    default String getId() {
        return ID;
    }

}
