package io.github.pizzaserver.format;

import io.github.pizzaserver.format.dimension.BedrockDimension;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public interface BedrockLevel extends Closeable {

    /**
     * Retrieve a specific dimension from this level.
     * @param dimensionId dimension id
     * @return dimension
     */
    BedrockDimension getDimension(int dimensionId);

    /**
     * Retrieve level data from the level.dat
     * @return level data
     */
    LevelData getLevelData();

    /**
     * Update the level.dat data file
     * @param data new level data
     * @throws IOException if failed to save
     */
    void setLevelData(LevelData data) throws IOException;

    /**
     * Retrieve the file/directory this level was created from.
     * @return the file
     */
    File getFile();

}
