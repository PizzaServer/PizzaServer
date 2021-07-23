package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;

public interface APILocation {

    float getX();

    float getY();

    float getZ();

    int getChunkX();

    int getChunkZ();

    APIChunk getChunk();

    APIWorld getWorld();



}
