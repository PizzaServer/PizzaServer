package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

public class BlockLocation extends Vector3i {

    private final World world;


    public BlockLocation(World world, Vector3i vector3i) {
        this(world, vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public BlockLocation(World world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
    }

    public Block getBlock() {
        return this.getWorld().getBlock(this);
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

    @Override
    public BlockLocation add(int x, int y, int z) {
        return new BlockLocation(this.world, this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @Override
    public BlockLocation subtract(int x, int y, int z) {
        return new BlockLocation(this.world, this.getX() - x, this.getY() - y, this.getZ() - z);
    }

    @Override
    public BlockLocation multiply(int x, int y, int z) {
        return new BlockLocation(this.world, this.getX() * x, this.getY() * y, this.getZ() * z);
    }

    @Override
    public BlockLocation divide(int x, int y, int z) {
        return new BlockLocation(this.world, this.getX() / x, this.getY() / y, this.getZ() / z);
    }

    public Location toLocation() {
        return new Location(this.getWorld(), this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockLocation) {
            BlockLocation otherBlockLocation = (BlockLocation) obj;
            return otherBlockLocation.getWorld().equals(this.getWorld())
                    && otherBlockLocation.getX() == this.getX()
                    && otherBlockLocation.getY() == this.getY()
                    && otherBlockLocation.getZ() == this.getZ();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (43 * this.getX()) + (43 * this.getY()) + (43 * this.getZ()) + (this.getWorld().hashCode());
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
