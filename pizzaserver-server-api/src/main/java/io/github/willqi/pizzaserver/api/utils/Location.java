package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;

public interface Location {

    float getX();

    float getY();

    float getZ();

    int getChunkX();

    int getChunkZ();

    Chunk getChunk();

    World getWorld();



}
