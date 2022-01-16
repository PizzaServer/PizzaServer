package io.github.pizzaserver.server.level.processing.requests;

import io.github.pizzaserver.api.level.world.World;

public abstract class ChunkRequest {

    private final World world;
    private final int x;
    private final int z;


    public ChunkRequest(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}
