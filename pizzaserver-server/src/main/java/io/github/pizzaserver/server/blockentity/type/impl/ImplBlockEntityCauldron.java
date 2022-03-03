package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockCauldron;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCauldron;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityCauldron extends BaseBlockEntity<BlockCauldron> implements BlockEntityCauldron {

    public ImplBlockEntityCauldron(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.CAULDRON);
    }

}
