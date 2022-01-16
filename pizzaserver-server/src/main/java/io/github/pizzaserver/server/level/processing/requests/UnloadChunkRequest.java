package io.github.pizzaserver.server.level.processing.requests;

import io.github.pizzaserver.api.level.world.World;

public class UnloadChunkRequest extends ChunkRequest {

    private final boolean forced;

    public UnloadChunkRequest(World world, int x, int z, boolean forced) {
        super(world, x, z);
        this.forced = forced;
    }

    public boolean isForced() {
        return this.forced;
    }
}
