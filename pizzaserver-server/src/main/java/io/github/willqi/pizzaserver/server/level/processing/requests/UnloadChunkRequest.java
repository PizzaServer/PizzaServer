package io.github.willqi.pizzaserver.server.level.processing.requests;

import io.github.willqi.pizzaserver.api.level.world.World;

public class UnloadChunkRequest extends ChunkRequest {

    public UnloadChunkRequest(World world, int x, int z) {
        super(world, x, z);
    }

}
