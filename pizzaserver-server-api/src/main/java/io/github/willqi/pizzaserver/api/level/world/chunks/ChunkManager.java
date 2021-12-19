package io.github.willqi.pizzaserver.api.level.world.chunks;

import io.github.willqi.pizzaserver.api.level.world.chunks.loader.ChunkLoader;
import io.github.willqi.pizzaserver.api.player.Player;

import java.io.Closeable;

public interface ChunkManager extends Closeable {

    boolean isChunkLoaded(int x, int z);

    /**
     * Retrieve a chunk from the cache or load it from the chunk provider.
     * @param x chunk x
     * @param z chunk z
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z);

    /**
     * Retrieve a chunk.
     * If it is not cached, it may load from the provider
     * @param x chunk x
     * @param z chunk z
     * @param loadFromProvider should it load from the world provider if not cached
     * @return {@link Chunk}
     */
    Chunk getChunk(int x, int z, boolean loadFromProvider);

    /**
     * Send a {@link Chunk} to a {@link Player} immediately.
     * The chunk will be fetched if it is not cached
     * @param player the {@link Player}
     * @param x chunk x chunk x
     * @param z chunk z chunk z
     */
    void sendChunk(Player player, int x, int z);

    /**
     * Send a {@link Chunk} to a {@link Player}.
     * @param player the {@link Player}
     * @param x chunk x
     * @param z chunk z
     * @param async if this should be done on a separate thread
     */
    void sendChunk(Player player, int x, int z, boolean async);

    /**
     * Add a {@link ChunkLoader} that keeps chunks loaded and ticking.
     * @param chunkLoader chunk loader
     * @return if the chunk loader was added
     */
    boolean addChunkLoader(ChunkLoader chunkLoader);

    /**
     * Remove a {@link ChunkLoader} that keeps chunks loaded and ticking.
     * @param chunkLoader chunk loader
     * @return if the chunk loader was removed
     */
    boolean removeChunkLoader(ChunkLoader chunkLoader);

}
