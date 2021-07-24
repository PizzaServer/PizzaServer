package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to serialize and send a chunk to a player
 */
public class ImplSendChunkToPlayerProcessingAction implements ChunkProcessingAction {

    private final ImplPlayer player;
    private final ImplChunk chunk;
    private final CompletableFuture<Void> response;


    public ImplSendChunkToPlayerProcessingAction(ImplPlayer player, ImplChunk chunk, CompletableFuture<Void> response) {
        this.player = player;
        this.chunk = chunk;
        this.response = response;
    }

    public ImplPlayer getPlayer() {
        return this.player;
    }

    public ImplChunk getChunk() {
        return this.chunk;
    }

    public CompletableFuture<Void> getResponse() {
        return this.response;
    }

}
