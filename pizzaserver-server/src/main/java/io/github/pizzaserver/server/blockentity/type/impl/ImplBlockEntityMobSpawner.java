package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.type.BlockEntityMobSpawner;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityMobSpawner extends BaseBlockEntity<BlockMobSpawner> implements BlockEntityMobSpawner {

    public ImplBlockEntityMobSpawner(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.MOB_SPAWNER);
    }

}
