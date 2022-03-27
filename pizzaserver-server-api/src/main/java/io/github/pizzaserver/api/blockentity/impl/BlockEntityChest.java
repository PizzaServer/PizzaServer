package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;

public class BlockEntityChest extends BlockEntityContainer<BlockChest> {

    public static final String ID = "Chest";


    public BlockEntityChest(BlockChest chest) {
        super(chest);
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }

}
