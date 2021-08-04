package io.github.willqi.pizzaserver.server.level.providers;

import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;

import java.io.Closeable;
import java.io.IOException;

/**
 * Handles world operations regarding loading/saving
 */
public abstract class BaseLevelProvider implements Closeable {

    /**
     * Retrieve the name of the world
     * @return
     */
    public abstract String getName();

    /**
     * Retrieve a chunk by it's x and z coordinates
     * @param x chunk x
     * @param z chunk z
     * @return the chunk
     */
    public abstract BedrockChunk getChunk(int x, int z, Dimension dimension) throws IOException;

}
