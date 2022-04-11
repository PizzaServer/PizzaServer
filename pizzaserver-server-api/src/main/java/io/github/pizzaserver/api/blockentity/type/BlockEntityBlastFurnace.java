package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockBlastFurnace;

public interface BlockEntityBlastFurnace extends BlockEntityFurnace {

    String ID = "BlastFurnace";

    @Override
    default String getId() {
        return ID;
    }

    @Override
    default BlockBlastFurnace getBlock() {
        return (BlockBlastFurnace) BlockEntityFurnace.super.getBlock();
    }

}
