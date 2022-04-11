package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityChest;

public class BlockEntityChestParser extends BlockEntityContainerParser<BlockChest>  {

    @Override
    public BlockEntityContainer<BlockChest> create(BlockLocation location) {
        return new ImplBlockEntityChest(location);
    }

}
