package io.github.pizzaserver.format.dimension.chunks.subchunk;

import io.github.pizzaserver.format.dimension.chunks.subchunk.utils.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a 16x16x16 chunk of blocks
 * Subchunks holds block layers which go up to 255 as of sub chunk version 8.
 */
public class BedrockSubChunk {

    private final List<BlockLayer> layers = new ArrayList<>();


    /**
     * Retrieve all of the {@link BlockLayer}s of this subchunk.
     * @return the {@link List} of {@link BlockLayer}s this subchunk holds.
     */
    public synchronized List<BlockLayer> getLayers() {
        return Collections.unmodifiableList(this.layers);
    }

    /**
     * Retrieve a specific {@link BlockLayer} in this subchunk.
     * If the layer does not exist, it should create the layer
     * @param index block layer index
     * @return {@link BlockLayer}
     */
    public synchronized BlockLayer getLayer(int index) {
        while (index >= this.getLayers().size()) {
            Palette<BlockPaletteEntry> blockPalette = new Palette<>();
            blockPalette.addEntry(new BlockPaletteEntry());    // ensure the palette has air

            BlockLayer blockLayer = new BlockLayer(blockPalette);
            this.addLayer(blockLayer);
        }
        return this.layers.get(index);
    }

    /**
     * Add a new {@link BlockLayer} to this subchunk.
     * @param layer The {@link BlockLayer} to add
     */
    public synchronized void addLayer(BlockLayer layer) {
        this.layers.add(layer);
    }

    public synchronized boolean isEmpty() {
        return this.layers.size() == 0;
    }

}
