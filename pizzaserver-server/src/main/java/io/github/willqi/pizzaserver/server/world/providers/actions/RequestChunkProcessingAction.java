package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;
import io.github.willqi.pizzaserver.server.world.World;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to fetch a chunk from the database
 */
public class RequestChunkProcessingAction implements ChunkProcessingAction {

    private final int x;
    private final int z;
    private final World world;
    private final CompletableFuture<APIChunk> response;


    public RequestChunkProcessingAction(World world, int x, int z, CompletableFuture<APIChunk> response) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.response = response;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public CompletableFuture<APIChunk> getResponseFuture() {
        return this.response;
    }

}
