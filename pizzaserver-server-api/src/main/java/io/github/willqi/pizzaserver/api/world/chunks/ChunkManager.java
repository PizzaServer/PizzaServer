package io.github.willqi.pizzaserver.api.world.chunks;

import io.github.willqi.pizzaserver.api.player.Player;

public interface ChunkManager {

    boolean isChunkLoaded(int x, int z);

    /**
     * Retrieve a chunk if it is cached
     * @param x chunk x
     * @param z chunk z
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z);

    /**
     * Retrieve a chunk
     * If it is not cached, it will retrieve the chunk from the world provider
     * @param x chunk x
     * @param z chunk z
     * @param loadFromProvider whether or not to load from world provider
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z, boolean loadFromProvider);

    /**
     * Unload a {@link Chunk}
     * Recommended to be called asynchronously
     * @param x x chunk coordinate
     * @param z z chunk cooridnate
     * @return if the chunk was unloaded
     */
    boolean unloadChunk(int x, int z);

    /**
     * Checks if a {@link Chunk} should be unloaded before unloading it
     * @param x chunk x
     * @param z chunk z
     * @return if the chunk was unloaded
     */
    boolean tryUnloadChunk(int x, int z);

    /**
     * Send a chunk to the player
     * If the chunk is not loaded, it will fetch the chunk from the provider
     * Recommended to be called asynchronously
     * @param player the {@link Player}
     * @param x chunk x
     * @param z chunk z
     */
    void sendChunk(Player player, int x, int z);

}
