package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityFurnace extends BlockEntity<BlockFurnace> {

    String ID = "Furnace";

    @Override
    default String getId() {
        return ID;
    }

}
