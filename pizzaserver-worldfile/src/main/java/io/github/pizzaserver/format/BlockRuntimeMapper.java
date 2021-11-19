package io.github.pizzaserver.format;

import com.nukkitx.nbt.NbtMap;

/**
 * Resolves block data to its Bedrock runtime id.
 */
public interface BlockRuntimeMapper {

    /**
     * Resolves to the runtime id of the block given id and block state of the block.
     * @param name id of the block (e.g. minecraft:air)
     * @param states {@link NbtMap} that has the block data for the specific block state
     * @return runtime id of the block
     */
    int getBlockRuntimeId(String name, NbtMap states);

}
