package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

public class BlockLocation extends Vector3i {

    private final World world;


    public BlockLocation(World world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
    }

    public Chunk getChunk() {
        return this.getWorld().getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
    }

    public World getWorld() {
        return this.world;
    }

    public Level getLevel() {
        return this.getWorld().getLevel();
    }

    public int getChunkX() {
        return (int)Math.floor(this.getX() / 16d);
    }

    public int getChunkZ() {
        return (int)Math.floor(this.getZ() / 16d);
    }

}
