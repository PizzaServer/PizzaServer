package io.github.willqi.pizzaserver.server.world.chunks.processing.requests;

import io.github.willqi.pizzaserver.api.player.Player;

public class PlayerChunkRequest extends ChunkRequest {

    private final Player player;


    public PlayerChunkRequest(Player player, int x, int z) {
        super(x, z);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
