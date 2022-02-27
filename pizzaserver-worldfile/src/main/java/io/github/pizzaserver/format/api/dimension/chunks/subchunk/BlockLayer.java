package io.github.pizzaserver.format.api.dimension.chunks.subchunk;

import io.github.pizzaserver.format.api.dimension.chunks.subchunk.utils.Palette;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * A BlockLayer is a layer of the blocks in a {@link BedrockSubChunk}.
 * A layer holds the palette entry of each block
 */
public class BlockLayer {

    private final Palette<BlockPaletteEntry> palette;
    private final int[] blocks = new int[4096];


    public BlockLayer(Palette<BlockPaletteEntry> palette) {
        this.palette = palette;
    }

    /**
     * Retrieve the {@link Palette} used for this layer.
     * @return the {@link Palette} used for this layer.
     */
    public Palette<BlockPaletteEntry> getPalette() {
        return this.palette;
    }

    /**
     * Retrieve the {@link BlockPaletteEntry} of a block at the given coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return the {@link BlockPaletteEntry} of the block at the coordinates
     */
    public BlockPaletteEntry getBlockEntryAt(int x, int y, int z) {
        if (this.palette.size() == 0) {
            // if the palette is empty, then add an air entry in order to make this.blocks accurately return air for all 0s.
            this.palette.addEntry(new BlockPaletteEntry());
        }

        return this.palette.getEntry(this.blocks[getBlockIndex(x, y, z)]);
    }

    /**
     * Set the coordinates of the blocklayer to a new {@link BlockPaletteEntry}.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param entry new entry to set the block to
     */
    public void setBlockEntryAt(int x, int y, int z, BlockPaletteEntry entry) {
        if (this.palette.size() == 0) {
            // If the palette is empty, then add an air entry to make every 0 in this.blocks return air.
            // Otherwise, when this method calls this.palette.add(entry), every 0 in this.blocks will be assigned that block.
            this.palette.addEntry(new BlockPaletteEntry());
        }

        this.palette.addEntry(entry);
        this.blocks[getBlockIndex(x, y, z)] = this.palette.getPaletteIndex(entry);
    }

    /**
     * Resize modifies the block layer values and removes unused block palette values.
     */
    public void resize() {
        // Get all the palette indexes being used
        Map<Integer, BlockPaletteEntry> usedEntries = new HashMap<>();
        for (int paletteIndex : this.blocks) {
            usedEntries.put(paletteIndex, this.palette.getEntry(paletteIndex));
        }

        // Remove unused palette entries
        Iterator<BlockPaletteEntry> entryIterator = new HashSet<>(this.palette.getEntries()).iterator();
        while (entryIterator.hasNext()) {
            BlockPaletteEntry entry = entryIterator.next();
            int paletteIndex = this.palette.getPaletteIndex(entry);

            // Air occupies the first element of the block palette and CANNOT be removed or else empty elements of
            // this.blocks will not resolve to air. Any other palette entry can be removed.
            if (!usedEntries.containsKey(paletteIndex) && !entry.getId().equals(BlockPaletteEntry.AIR_ID)) {
                entryIterator.remove();
                this.palette.removeEntry(entry);
            }
        }

        // Shift entries in the palette as far down as possible
        this.palette.resize();

        // Update our blocks with the new palette indexes
        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i] = this.palette.getPaletteIndex(usedEntries.get(this.blocks[i]));
        }
    }

    private static int getBlockIndex(int x, int y, int z) {
        return (x << 8) | (z << 4) | y;
    }

}
