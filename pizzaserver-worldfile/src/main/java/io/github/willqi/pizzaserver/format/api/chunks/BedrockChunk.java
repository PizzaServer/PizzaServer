package io.github.willqi.pizzaserver.format.api.chunks;

import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;

import java.util.List;

/**
 * Represents a 16x16 chunk of blocks in a Minecraft world
 */
public interface BedrockChunk extends BedrockNetworkDiskSerializable {

    int getX();

    int getZ();

    Dimension getDimension();

    /**
     * A height map is a array of 256 (16 * 16) integers that stores the highest blocks in a chunk
     * @return int array
     */
    int[] getHeightMap();

    /**
     * Retrieve the tallest block at a specific coordinate
     * @param position The {@link Vector2i} that is representative of the coordinates in the chunk we need the height of.
     * @return height
     */
    int getHighestBlockAt(Vector2i position);

    /**
     * Retrieve the tallest block at a specific coordinate
     * @param x x coordinate
     * @param z z coordinate
     * @return height
     */
    int getHighestBlockAt(int x, int z);

    /**
     * Change the height map at a specific block in this chunk
     * @param position coordinates
     * @param newHeight new height
     */
    void setHighestBlockAt(Vector2i position, int newHeight);

    /**
     * Change the height map at a specific block in this chunk
     * @param x x coordinate
     * @param z z coordinate
     * @param newHeight new height
     */
    void setHighestBlockAt(int x, int z, int newHeight);

    /**
     * An array of 256 (16 * 16) bytes that stores the biomes in a chunk
     * @return byte array
     */
    byte[] getBiomeData();

    /**
     * Retrieve the biome data at a specific coordinate
     * @param position The {@link Vector2i} that is representative of the coordinates in the chunk we need the biome of
     * @return the biome
     */
    byte getBiomeAt(Vector2i position);

    /**
     * Retrieve the biome data at a specific coordinate
     * @param x x coordinate
     * @param z z coordinate
     * @return the biome
     */
    byte getBiomeAt(int x, int z);

    /**
     * Set the biome data at a specific position
     * @param position The {@link Vector2i} that is representative of the coordinates in the chunk we are changing the biome of
     * @param biome the new biome
     */
    void setBiomeAt(Vector2i position, byte biome);

    /**
     * Set the biome data at a specific location
     * @param x x coordinate
     * @param z z coordinate
     * @param biome the new biome we are changing this block column to
     */
    void setBiomeAt(int x, int z, byte biome);

    int getSubChunkCount();

    /**
     * Retrieve all of the {@link BedrockSubChunk}s in this chunk.
     * This must be in ascending order
     * @return list of the subchunks
     */
    List<BedrockSubChunk> getSubChunks();

    /**
     * Retrieve a specific {@link BedrockSubChunk} in this chunk
     * @param index the index of the subchunk
     * @return the subchunk requested
     */
    BedrockSubChunk getSubChunk(int index);

}
