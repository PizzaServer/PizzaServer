package io.github.willqi.pizzaserver.server.utils;

import io.github.willqi.pizzaserver.api.utils.APILocation;
import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

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
    public APIChunk getChunk() {
        if (this.getWorld().getChunkManager().isChunkLoaded(this.getChunkX(), this.getChunkZ())) {
            return this.getWorld().getChunkManager().getChunk(this.getChunkX(), this.getChunkZ());
        }
        return null;
    }

    @Override
    public APIWorld getWorld() {
        return this.world;
    }

}
