package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.blockentity.type.BlockEntitySmoker;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntitySmoker;

public class BlockEntitySmokerParser extends BlockEntityFurnaceParser {

    @Override
    public BlockEntitySmoker create(BlockLocation location) {
        return new ImplBlockEntitySmoker(location);
    }

}
