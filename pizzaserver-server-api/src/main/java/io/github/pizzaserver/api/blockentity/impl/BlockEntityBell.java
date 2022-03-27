package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityBell extends BaseBlockEntity<BlockBell> {

    public static final String ID = "Bell";


    public BlockEntityBell(BlockBell bell) {
        super(bell);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
