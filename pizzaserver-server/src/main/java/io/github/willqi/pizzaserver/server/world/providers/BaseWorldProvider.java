package io.github.willqi.pizzaserver.server.world.providers;

import io.github.willqi.pizzaserver.format.api.LevelData;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;

import java.io.Closeable;
import java.io.IOException;

/**
 * Handles world operations regarding loading/saving
 */
public abstract class BaseWorldProvider implements Closeable {

    /**
     * Retrieve information about the level
     * @return {@link LevelData}
     */
    public abstract LevelData getLevelData();

    /**
     * Retrieve a chunk by it's x and z coordinates
     * @param x chunk x
     * @param z chunk z
     * @return the chunk
     */
    public abstract BedrockChunk getChunk(int x, int z) throws IOException;

}
