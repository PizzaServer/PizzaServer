package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;

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
        if (this.getWorld() == null) {
            return  null;
        }
        return this.getWorld().getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
    }

    public World getWorld() {
        return this.world;
    }

    public Level getLevel() {
        if (this.getWorld() == null) {
            return null;
        }
        return this.getWorld().getLevel();
    }

    public int getChunkX() {
        return (int)Math.floor(this.getX() / 16d);
    }

    public int getChunkZ() {
        return (int)Math.floor(this.getZ() / 16d);
    }

    @Override
    public String toString() {
        return "Location(level=" + this.getWorld().getLevel().getName() + ", world=" + this.getWorld().getDimension() + ", x=" + this.getX() +",y=" + this.getY() + ",z=" + this.getZ() + ")";
    }

}
