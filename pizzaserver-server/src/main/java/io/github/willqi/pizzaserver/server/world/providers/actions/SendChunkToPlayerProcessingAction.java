package io.github.willqi.pizzaserver.server.world.providers.actions;

import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

public class SendChunkToPlayerProcessingAction implements ChunkProcessingAction {

    private final Player player;
    private final Chunk chunk;


    public SendChunkToPlayerProcessingAction(Player player, Chunk chunk) {
        this.player = player;
        this.chunk = chunk;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

}
