package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.blockentity.type.BlockEntityBlastFurnace;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityBlastFurnace;

public class BlockEntityBlastFurnaceParser extends BlockEntityFurnaceParser {

    @Override
    public BlockEntityBlastFurnace create(BlockLocation location) {
        return new ImplBlockEntityBlastFurnace(location);
    }

}
