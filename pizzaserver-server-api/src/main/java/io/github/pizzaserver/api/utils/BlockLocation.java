package io.github.pizzaserver.api.utils;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;

public class BlockLocation {

    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final int layer;


    public BlockLocation(World world, Vector3i vector3i) {
        this(world, vector3i.getX(), vector3i.getY(), vector3i.getZ(), 0);
    }

    public BlockLocation(World world, Vector3i vector3i, int layer) {
        this(world, vector3i.getX(), vector3i.getY(), vector3i.getZ(), layer);
    }

    public BlockLocation(World world, int x, int y, int z, int layer) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.layer = layer;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getLayer() {
        return this.layer;
    }

    public Block getBlock() {
        return this.getWorld().getBlock(this.getX(), this.getY(), this.getZ(), this.getLayer());
    }

    public Chunk getChunk() {
        return this.getWorld().getChunk(this.getChunkX(), this.getChunkZ());
    }

    public World getWorld() {
        return this.world;
    }

    public Level getLevel() {
        return this.getWorld().getLevel();
    }

    public int getChunkX() {
        return ((int) Math.floor(this.getX() / 16d));
    }

    public int getChunkZ() {
        return ((int) Math.floor(this.getZ() / 16d));
    }

    public Vector3i toVector3i() {
        return Vector3i.from(this.getX(), this.getY(), this.getZ());
    }

    public Vector3f toVector3f() {
        return Vector3f.from(this.getX(), this.getY(), this.getZ());
    }

    public Location toLocation() {
        return new Location(this.getWorld(), this.toVector3i());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockLocation otherBlockLocation) {
            return this.getWorld().equals(otherBlockLocation.getWorld())
                    && otherBlockLocation.getX() == this.getX()
                    && otherBlockLocation.getY() == this.getY()
                    && otherBlockLocation.getZ() == this.getZ();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (43 * this.getX())
                + (43 * this.getY())
                + (43 * this.getZ())
                + (43 * (this.getWorld() != null ? this.getWorld().hashCode() : 0));
    }

    @Override
    public String toString() {
        return "BlockLocation(level=" + this.getLevel().getName()
                + ", world=" + this.getWorld().getDimension()
                + ", x=" + this.getX()
                + ", y=" + this.getY()
                + ", z=" + this.getZ() + ")";
    }

}
