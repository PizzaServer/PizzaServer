package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

public class BlockEntityBlastFurnace extends BlockEntityFurnace {

    public static final String ID = "BlastFurnace";


    public BlockEntityBlastFurnace(Vector3i blockPosition) {
        super(blockPosition);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
