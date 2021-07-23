package io.github.willqi.pizzaserver.server.utils;

import io.github.willqi.pizzaserver.api.utils.APILocation;
import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.world.World;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;

public class Location extends Vector3 implements APILocation {

    private final APIWorld world;

    public Location(APIWorld world, Vector3 position) {
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
        if (((World)this.getWorld()).getChunkManager().isChunkLoaded(this.getChunkX(), this.getChunkZ())) {
            return ((World)this.world).getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
        }
        return null;
    }

    @Override
    public APIWorld getWorld() {
        return this.world;
    }

}
