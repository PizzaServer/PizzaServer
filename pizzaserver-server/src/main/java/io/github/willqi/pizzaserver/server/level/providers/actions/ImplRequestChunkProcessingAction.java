package io.github.willqi.pizzaserver.server.level.providers.actions;

import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to fetch a chunk from the database
 */
public class ImplRequestChunkProcessingAction implements ChunkProcessingAction {

    private final int x;
    private final int z;
    private final ImplWorld world;
    private final CompletableFuture<Chunk> response;


    public ImplRequestChunkProcessingAction(ImplWorld world, int x, int z, CompletableFuture<Chunk> response) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.response = response;
    }

    public ImplWorld getWorld() {
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
