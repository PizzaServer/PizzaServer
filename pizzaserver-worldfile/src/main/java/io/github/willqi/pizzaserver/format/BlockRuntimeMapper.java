package io.github.willqi.pizzaserver.format;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

/**
 * Resolves block data to it's Bedrock runtime id
 */
public interface BlockRuntimeMapper {

    /**
     * Resolves to the runtime id of the block given id and block state of the block
     * @param name id of the block (e.g. minecraft:air)
     * @param states {@link NBTCompound} that has the block data for the specific block state
     * @return runtime id of the block
     */
    int getBlockRuntimeId(String name, NBTCompound states);

}
