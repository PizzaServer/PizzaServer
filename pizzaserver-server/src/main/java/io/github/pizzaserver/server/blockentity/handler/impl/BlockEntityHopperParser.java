package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.block.impl.BlockHopper;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityHopper;

public class BlockEntityHopperParser extends BlockEntityContainerParser<BlockHopper> {

    @Override
    public BlockEntityContainer<BlockHopper> create(BlockLocation location) {
        return new ImplBlockEntityHopper(location);
    }

}
