package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockSmoker;

public interface BlockEntitySmoker extends BlockEntityFurnace {

    String ID = "Smoker";

    @Override
    default String getId() {
        return ID;
    }

    @Override
    default BlockSmoker getBlock() {
        return (BlockSmoker) BlockEntityFurnace.super.getBlock();
    }

}
