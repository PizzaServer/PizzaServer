package io.github.pizzaserver.server.level.processing.requests;

import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.server.player.ImplPlayer;

public class PlayerChunkRequest extends ChunkRequest {

    private final ImplPlayer player;


    public PlayerChunkRequest(ImplPlayer player, int x, int z) {
        super(player.expect(EntityKeys.WORLD), x, z);
        this.player = player;
    }

    public ImplPlayer getPlayer() {
        return this.player;
    }

}
