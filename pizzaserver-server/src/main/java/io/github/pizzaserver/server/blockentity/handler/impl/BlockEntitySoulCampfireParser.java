package io.github.pizzaserver.server.blockentity.handler.impl;

import io.github.pizzaserver.api.blockentity.type.BlockEntitySoulCampfire;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntitySoulCampfire;

public class BlockEntitySoulCampfireParser extends BlockEntityCampfireParser {

    @Override
    public BlockEntitySoulCampfire create(BlockLocation location) {
        return new ImplBlockEntitySoulCampfire(location);
    }

}
