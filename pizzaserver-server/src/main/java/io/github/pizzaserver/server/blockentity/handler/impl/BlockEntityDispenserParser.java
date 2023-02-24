package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.block.impl.BlockDispenser;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityDispenser;

public class BlockEntityDispenserParser extends BlockEntityContainerParser<BlockDispenser> {

    @Override
    public BlockEntityContainer<BlockDispenser> create(BlockLocation location) {
        return new ImplBlockEntityDispenser(location);
    }

}
