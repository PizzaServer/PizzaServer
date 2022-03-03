package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.block.impl.BlockBarrel;
import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityBarrel;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityChest;

public class BlockEntityBarrelParser extends BlockEntityContainerParser<BlockBarrel>  {

    @Override
    public BlockEntityContainer<BlockBarrel> create(BlockLocation location) {
        return new ImplBlockEntityBarrel(location);
    }

}
