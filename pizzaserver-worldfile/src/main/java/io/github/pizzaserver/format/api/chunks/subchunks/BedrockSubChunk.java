package io.github.pizzaserver.format.api.chunks.subchunks;

import io.github.pizzaserver.format.api.chunks.BedrockNetworkDiskSerializable;

import java.util.List;

/**
 * Represents a 16x16x16 chunk of blocks
 * Subchunks holds block layers which go up to 255 as of sub chunk version 8.
 */
public interface BedrockSubChunk extends BedrockNetworkDiskSerializable {

    /**
     * Retrieve all of the {@link BlockLayer}s of this subchunk.
     * @return the {@link List} of {@link BlockLayer}s this subchunk holds.
     */
    List<BlockLayer> getLayers();

    /**
     * Retrieve a specific {@link BlockLayer} in this subchunk.
     * If the layer does not exist, it should create the layer
     * @param index block layer index
     * @return {@link BlockLayer}
     */
    BlockLayer getLayer(int index);

    /**
     * Add a new {@link BlockLayer} to this subchunk.
     * @param layer The {@link BlockLayer} to add
     */
    void addLayer(BlockLayer layer);
}
