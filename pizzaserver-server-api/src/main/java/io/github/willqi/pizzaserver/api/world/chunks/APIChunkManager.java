package io.github.willqi.pizzaserver.api.world.chunks;

import java.util.concurrent.CompletableFuture;

public interface APIChunkManager {

    boolean isChunkLoaded(int x, int z);

    APIChunk getChunk(int x, int z);

    /**
     * Unload a {@link APIChunk} asynchronously.
     * @param x x chunk coordinate
     * @param z z chunk cooridnate
     */
    CompletableFuture<Void> unloadChunk(int x, int z);

    /**
     * Fetch a {@link APIChunk} given the chunk x and z coordinates
     * @param x chunk x chunk x
     * @param z chunk z chunk z
     * @return {@link APIChunk} at these coordinates
     */
    CompletableFuture<APIChunk> fetchChunk(int x, int z);

}
