package io.github.pizzaserver.api.utils;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;

public class Location {

    private final World world;
    private final float x;
    private final float y;
    private final float z;

    public Location(World world, Vector3i vector3i) {
        this(world, Vector3f.from(vector3i.getX(), vector3i.getY(), vector3i.getZ()));
    }

    public Location(World world, Vector3f vector3) {
        this(world, vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Location(World world, float x, float y, float z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public Chunk getChunk() {
        if (this.getWorld() == null) {
            return null;
        }
        return this.getWorld().getChunk(this.getChunkX(), this.getChunkZ());
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
        return (int) Math.floor(this.getX() / 16d);
    }

    public int getChunkZ() {
        return (int) Math.floor(this.getZ() / 16d);
    }

    public Vector3f toVector3f() {
        return Vector3f.from(this.getX(), this.getY(), this.getZ());
    }

    public Vector3i toVector3i() {
        return Vector3i.from(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public String toString() {
        return "Location(level=" + this.getWorld().getLevel().getName() + ", world=" + this.getWorld().getDimension()
                + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
    }
}
