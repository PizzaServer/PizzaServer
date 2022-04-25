package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityCampfire extends BlockEntity<BlockCampfire> {

    String ID = "Campfire";

    @Override
    default String getId() {
        return ID;
    }

}
