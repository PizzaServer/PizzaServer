package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to serialize and send a chunk to a player
 */
public class SendChunkToPlayerProcessingAction implements ChunkProcessingAction {

    private final Player player;
    private final Chunk chunk;
    private final CompletableFuture<Void> response;


    public SendChunkToPlayerProcessingAction(Player player, Chunk chunk, CompletableFuture<Void> response) {
        this.player = player;
        this.chunk = chunk;
        this.response = response;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public CompletableFuture<Void> getResponse() {
        return this.response;
    }

}
