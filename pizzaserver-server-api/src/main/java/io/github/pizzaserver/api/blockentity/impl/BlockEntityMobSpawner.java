package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityMobSpawner extends BaseBlockEntity {

    public static final String ID = "MobSpawner";


    public BlockEntityMobSpawner(BlockLocation blockPosition) {
        super(blockPosition);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
