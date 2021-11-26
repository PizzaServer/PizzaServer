package io.github.pizzaserver.api.blockentity.types.impl;

import io.github.pizzaserver.api.blockentity.impl.BlockEntityChest;
import io.github.pizzaserver.api.blockentity.types.BaseBlockEntityType;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.pizzaserver.api.level.world.blocks.types.BlockType;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

public class BlockEntityTypeChest extends BaseBlockEntityType {

    public static final String ID = "Chest";


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public BlockType getBlockType() {
        return BlockRegistry.getBlockType(BlockTypeID.CHEST);
    }

    @Override
    public BlockEntityChest create(Block block) {
        return new BlockEntityChest(block);
    }

}
