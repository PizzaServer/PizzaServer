package io.github.willqi.pizzaserver.server.utils;

import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class ImplLocation extends Vector3 implements Location {

    private final World world;

    public ImplLocation(World world, Vector3 position) {
        super(position.getX(), position.getY(), position.getZ());
        this.world = world;
    }

    @Override
    public int getChunkX() {
        return (int)this.getX() / 16;
    }

    @Override
    public int getChunkZ() {
        return (int)this.getZ() / 16;
    }

    /**
     * Return the loaded chunk
     * @return the chunk or null if the chunk is not yet loaded.
     */
    @Override
    public Chunk getChunk() {
        return this.getWorld().getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
    }

    @Override
    public Level getLevel() {
        return this.getWorld().getLevel();
    }

    @Override
    public World getWorld() {
        return this.world;
    }

}
