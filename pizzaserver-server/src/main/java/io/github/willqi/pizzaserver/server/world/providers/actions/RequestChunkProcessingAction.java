package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.BedrockWorld;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to fetch a chunk from the database
 */
public class RequestChunkProcessingAction implements ChunkProcessingAction {

    private final int x;
    private final int z;
    private final BedrockWorld world;
    private final CompletableFuture<Chunk> response;


    public RequestChunkProcessingAction(BedrockWorld world, int x, int z, CompletableFuture<Chunk> response) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.response = response;
    }

    public BedrockWorld getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public CompletableFuture<Chunk> getResponseFuture() {
        return this.response;
    }

}
