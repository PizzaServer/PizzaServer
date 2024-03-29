package io.github.pizzaserver.api.packs;

import java.util.UUID;

public interface ResourcePack {

    /**
     * Retrieve the max size of each data pack chunk sent to the Minecraft client.
     * @return max chunk size sent over the network
     */
    int getMaxChunkLength();

    /**
     * Retrieve the {@link UUID} of the data pack.
     * @return {@link UUID} of the player
     */
    UUID getUuid();

    /**
     * Retrieve the version of the data pack.
     * @return data pack version
     */
    String getVersion();

    boolean isRayTracingEnabled();

    /**
     * Retrieve the total length of the data pack contents.
     * @return total length of contents
     */
    long getDataLength();

    /**
     * Retrieve the amount of chunks this data pack is split into.
     * @return amount of chunks this resource pack is split into
     */
    int getChunkCount();

    /**
     * SHA-256 of the contents of the datapack.
     * @return hash SHA-256 hash
     */
    byte[] getHash();

    /**
     * Get a chunk of the data to be sent over the network.
     * @param index chunk index
     * @return chunk data
     */
    byte[] getChunk(int index);

}
