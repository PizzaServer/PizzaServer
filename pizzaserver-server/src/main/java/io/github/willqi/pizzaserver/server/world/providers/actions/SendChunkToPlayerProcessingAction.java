package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.server.player.BedrockPlayer;
import io.github.willqi.pizzaserver.server.world.chunks.BedrockChunk;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a request to serialize and send a chunk to a player
 */
public class SendChunkToPlayerProcessingAction implements ChunkProcessingAction {

    private final BedrockPlayer player;
    private final BedrockChunk chunk;
    private final CompletableFuture<Void> response;


    public SendChunkToPlayerProcessingAction(BedrockPlayer player, BedrockChunk chunk, CompletableFuture<Void> response) {
        this.player = player;
        this.chunk = chunk;
        this.response = response;
    }

    public BedrockPlayer getPlayer() {
        return this.player;
    }

    public BedrockChunk getChunk() {
        return this.chunk;
    }

    public CompletableFuture<Void> getResponse() {
        return this.response;
    }

}
