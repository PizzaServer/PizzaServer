package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

public class BlockEntityChest extends BlockEntityContainer {

    public static final String ID = "Chest";


    public BlockEntityChest(Vector3i blockLocation) {
        super(blockLocation);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
