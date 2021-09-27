package io.github.willqi.pizzaserver.server.level.providers;

import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.format.api.LevelData;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Handles world operations regarding loading/saving.
 */
public abstract class BaseLevelProvider implements Closeable {

    private final File file;


    public BaseLevelProvider(File file) {
        this.file = file;
    }

    /**
     * Retrieve information about the level.
     * @return {@link LevelData}
     */
    public abstract LevelData getLevelData();

    /**
     * Retrieve a chunk by it's x and z coordinates.
     * @param x chunk x
     * @param z chunk z
     * @return the chunk
     */
    public abstract BedrockChunk getChunk(int x, int z, Dimension dimension) throws IOException;

    /**
     * Get the name of the file for this level.
     * @return name of the file for this level
     */
    public String getFileName() {
        return this.file.getName();
    }

}
