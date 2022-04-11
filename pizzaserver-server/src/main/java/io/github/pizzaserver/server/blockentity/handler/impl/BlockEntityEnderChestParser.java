package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.blockentity.type.BlockEntityEnderChest;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityEnderChest;

public class BlockEntityEnderChestParser extends BaseBlockEntityParser<BlockEntityEnderChest> {

    @Override
    public BlockEntityEnderChest create(BlockLocation location) {
        return new ImplBlockEntityEnderChest(location);
    }

}
