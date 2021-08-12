package io.github.willqi.pizzaserver.server.world.chunks.processing.requests;

public abstract class ChunkRequest {

    private final int x;
    private final int z;


    public ChunkRequest(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

}
