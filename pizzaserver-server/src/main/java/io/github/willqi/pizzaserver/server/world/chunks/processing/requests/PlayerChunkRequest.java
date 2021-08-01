package io.github.willqi.pizzaserver.server.world.chunks.processing.requests;

import io.github.willqi.pizzaserver.server.player.ImplPlayer;

public class PlayerChunkRequest extends ChunkRequest {

    private final ImplPlayer player;


    public PlayerChunkRequest(ImplPlayer player, int x, int z) {
        super(x, z);
        this.player = player;
    }

    public ImplPlayer getPlayer() {
        return this.player;
    }

}
