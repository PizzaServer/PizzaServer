package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.block.impl.BlockDropper;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityDropper;

public class BlockEntityDropperParser extends BlockEntityContainerParser<BlockDropper> {

    @Override
    public BlockEntityContainer<BlockDropper> create(BlockLocation location) {
        return new ImplBlockEntityDropper(location);
    }

}
