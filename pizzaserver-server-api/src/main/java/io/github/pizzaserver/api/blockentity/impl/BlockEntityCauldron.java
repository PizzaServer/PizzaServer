package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityCauldron extends BaseBlockEntity {

    public static final String ID = "Cauldron";


    public BlockEntityCauldron(BlockLocation blockPosition) {
        super(blockPosition);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
