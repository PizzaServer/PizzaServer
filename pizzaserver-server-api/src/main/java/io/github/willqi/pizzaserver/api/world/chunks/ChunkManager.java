package io.github.willqi.pizzaserver.api.world.chunks;

import java.util.concurrent.CompletableFuture;

public interface ChunkManager {

    boolean isChunkLoaded(int x, int z);

    Chunk getChunk(int x, int z);

    /**
     * Unload a {@link Chunk} asynchronously.
     * @param x x chunk coordinate
     * @param z z chunk cooridnate
     */
    CompletableFuture<Void> unloadChunk(int x, int z);

    /**
     * Fetch a {@link Chunk} given the chunk x and z coordinates
     * @param x chunk x chunk x
     * @param z chunk z chunk z
     * @return {@link Chunk} at these coordinates
     */
    CompletableFuture<Chunk> fetchChunk(int x, int z);

}
