package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityCampfire extends BaseBlockEntity<BlockCampfire> {

    public static final String ID = "Campfire";


    public BlockEntityCampfire(BlockCampfire campfire) {
        super(campfire);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
