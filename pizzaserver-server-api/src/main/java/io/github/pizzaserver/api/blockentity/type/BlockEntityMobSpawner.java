package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityMobSpawner extends BlockEntity<BlockMobSpawner> {

    String ID = "MobSpawner";

    @Override
    default String getId() {
        return ID;
    }

}
