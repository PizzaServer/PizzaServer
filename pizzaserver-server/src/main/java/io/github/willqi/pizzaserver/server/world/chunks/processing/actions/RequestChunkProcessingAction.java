package io.github.willqi.pizzaserver.server.world.chunks.processing.actions;

import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

import java.util.concurrent.CompletableFuture;

public class RequestChunkProcessingAction implements ChunkProcessingAction {

    private final int x;
    private final int z;
    private final CompletableFuture<Chunk> response;


    public RequestChunkProcessingAction(int x, int z, CompletableFuture<Chunk> response) {
        this.x = x;
        this.z = z;
        this.response = response;
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
