package io.github.willqi.pizzaserver.server.utils;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.world.World;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

public class Location extends Vector3 {

    private final World world;

    public Location(World world, Vector3 position) {
        super(position.getX(), position.getY(), position.getZ());
        this.world = world;
    }

    public int getChunkX() {
        return (int)this.getX() / 16;
    }

    public int getChunkZ() {
        return (int)this.getZ() / 16;
    }

    /**
     * Return the loaded chunk
     * @return the chunk or null if the chunk is not yet loaded.
     */
    public Chunk getChunk() {
        if (this.getWorld().getChunkManager().isChunkLoaded(this.getChunkX(), this.getChunkZ())) {
            return this.world.getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
        }
        return null;
    }

    public World getWorld() {
        return this.world;
    }

}
