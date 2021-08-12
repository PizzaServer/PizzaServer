package io.github.willqi.pizzaserver.api.world.chunks;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.World;

import java.io.Closeable;

public interface ChunkManager extends Closeable {

    boolean isChunkLoaded(int x, int z);

    /**
     * Retrieve a chunk from the cache or load it from the chunk provider
     * @param x chunk x
     * @param z chunk z
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z);

    /**
     * Retrieve a chunk
     * If it is not cached, it may load from the provider
     * @param x chunk x
     * @param z chunk z
     * @param loadFromProvider whether or not to load from world provider
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z, boolean loadFromProvider);

    /**
     * Try to unload a {@link Chunk} and save it to disk immediately
     * @param x x chunk coordinate
     * @param z z chunk coordinate
     */
    void unloadChunk(int x, int z);

    /**
     * Try to unload a {@link Chunk} and save it to disk
     * @param x x chunk coordinate
     * @param z z chunk coordinate
     * @param async if this should be done on a separate thread
     * @param force Forcefully unload a chunk regardless of whether or not it can be closed
     */
    void unloadChunk(int x, int z, boolean async, boolean force);

    /**
     * Send a {@link Chunk} to a {@link Player} immediately.
     * The chunk will be fetched if it is not cached
     * @param player the {@link Player}
     * @param x chunk x chunk x
     * @param z chunk z chunk z
     */
    void sendPlayerChunk(Player player, int x, int z);

    /**
     * Send a {@link Chunk} to a {@link Player}
     * @param player the {@link Player}
     * @param x chunk x
     * @param z chunk z
     * @param async if this should be done on a separate thread
     */
    void sendPlayerChunk(Player player, int x, int z, boolean async);

    World getWorld();

}
