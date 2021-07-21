package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;

/**
 * A BlockLayer is a layer of the blocks in a {@link BedrockSubChunk}.
 * A layer holds the palette entry of each block
 */
public interface BlockLayer extends BedrockNetworkDiskSerializable {

    /**
     * Retrieve the {@link BlockPalette} used for this layer
     * @return the {@link BlockPalette} used for this layer
     */
    BlockPalette getPalette();

    /**
     * Retrieve the {@link BlockPalette.Entry} of a block at the given coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return the {@link BlockPalette.Entry} of the block at the coordinates
     */
    BlockPalette.Entry getBlockEntryAt(int x, int y, int z);

    /**
     * Set the coordinates of the blocklayer to a new {@link BlockPalette.Entry}
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param entry new entry to set the block to
     */
    void setBlockEntryAt(int x, int y, int z, BlockPalette.Entry entry);

}
