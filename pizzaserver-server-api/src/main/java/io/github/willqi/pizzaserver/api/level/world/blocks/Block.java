package io.github.willqi.pizzaserver.api.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public interface Block {

    BlockType getBlockType();

    /**
     * The block state data of the block
     * This is equivalent to a block state stored in the block_states.nbt file
     * @return {@link NBTCompound} of the block state data
     */
    NBTCompound getBlockState();

    int getBlockStateIndex();

    void setBlockStateIndex(int index);

}
