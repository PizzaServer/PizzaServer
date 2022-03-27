package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityMobSpawner extends BaseBlockEntity<BlockMobSpawner> {

    public static final String ID = "MobSpawner";


    public BlockEntityMobSpawner(BlockMobSpawner mobSpawner) {
        super(mobSpawner);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
