package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

public class Location extends Vector3 {

    private final World world;

    public Location(World world, Vector3i vector3i) {
        this(world, vector3i.toVector3());
    }

    public Location(World world, Vector3 vector3) {
        this(world, vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Location(World world, float x, float y, float z) {
        super(x, y, z);
        this.world = world;
    }

    public Chunk getChunk() {
        if (this.world.getChunkManager().isChunkLoaded(this.getChunkX(), this.getChunkZ())) {
            return this.world.getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
        }
        return null;
    }

    public World getWorld() {
        return this.world;
    }

    public int getChunkX() {
        return (int)(this.getX() / 16);
    }

    public int getChunkZ() {
        return (int)(this.getZ() / 16);
    }

}
