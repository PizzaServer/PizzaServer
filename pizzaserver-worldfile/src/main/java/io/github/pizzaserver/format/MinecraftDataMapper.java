package io.github.pizzaserver.format;

import com.nukkitx.nbt.NbtMap;

/**
 * Resolves block data to its Bedrock runtime id.
 */
public interface MinecraftDataMapper {

    /**
     * Resolves to the runtime id of the block given id and block state of the block.
     *
     * @param name   id of the block (e.g. minecraft:air)
     * @param states NBT that has the block data for the specific block state
     * @return runtime id of the block
     */
    int getBlockRuntimeId(String name, NbtMap states);

    /**
     * Resolves disk block entity NBT to network block entity NBT.
     *
     * @param diskBlockEntityNBT disk NBT
     * @return network NBT
     */
    NbtMap getNetworkBlockEntityNBT(NbtMap diskBlockEntityNBT);
}
