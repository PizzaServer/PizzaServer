package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCampfire;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Set;

public class ImplBlockEntityCampfire extends BaseBlockEntity<BlockCampfire> implements BlockEntityCampfire {

    public ImplBlockEntityCampfire(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return Set.of(BlockID.CAMPFIRE, BlockID.SOUL_CAMPFIRE);
    }

    @Override
    public String getId() {
        return BlockEntityCampfire.ID;
    }

}
