package io.github.pizzaserver.format.mcworld.world.chunks.subchunks;

import io.github.pizzaserver.format.MinecraftDataMapper;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.netty.buffer.ByteBuf;
import net.daporkchop.lib.common.function.io.IOConsumer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MCWorldBlockLayer implements BlockLayer {

    private final MCWorldBlockPalette palette;
    private final int[] blocks = new int[4096];


    public MCWorldBlockLayer(MCWorldBlockPalette palette) {
        this.palette = palette;
    }

    /**
     * Create a block layer based off of mcworld data.
     *
     * @param palette       the block palette
     * @param buffer        the buffer that needs to be parsed (mcworld data)
     * @param bitsPerBlock  amount of bits per block
     * @param blocksPerWord amount of minecraft blocks that are in each word
     * @param wordsPerChunk amount of LE ints that contain all the blocks in this layer
     */
    public MCWorldBlockLayer(
            MCWorldBlockPalette palette, ByteBuf buffer, int bitsPerBlock, int blocksPerWord, int wordsPerChunk) {
        this(palette);
        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = buffer.readIntLE();  // This integer can store multiple minecraft blocks.
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) {
                    break;
                }

                int paletteIndex = (word >> (pos % blocksPerWord) * bitsPerBlock) & ((1 << bitsPerBlock) - 1);
                this.blocks[pos] = paletteIndex;
                pos++;
            }
        }
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }

    @Override
    public synchronized BlockPalette.Entry getBlockEntryAt(int x, int y, int z) {
        if (this.palette.size() == 0) {
            // if the palette is empty, then add an air entry in order to make this.blocks accurately return air for all 0s.
            this.palette.add(new BlockPalette.EmptyEntry());
        }

        return this.palette.getEntry(this.blocks[getBlockIndex(x, y, z)]);
    }

    @Override
    public synchronized void setBlockEntryAt(int x, int y, int z, BlockPalette.Entry entry) {
        if (this.palette.size() == 0) {
            // If the palette is empty, then add an air entry to make every 0 in this.blocks return air.
            // Otherwise, when this method calls this.palette.add(entry), every 0 in this.blocks will be assigned that block.
            this.palette.add(new BlockPalette.EmptyEntry());
        }

        this.palette.add(entry);
        this.blocks[getBlockIndex(x, y, z)] = this.palette.getPaletteIndex(entry);
    }

    @Override
    public void serializeForDisk(ByteBuf buffer) {
        this.cleanUpPalette();
        this.serialize(buffer, (palette) -> this.palette.serializeForDisk(buffer));
    }

    @Override
    public void serializeForNetwork(ByteBuf buffer, MinecraftDataMapper runtimeMapper) {
        this.serialize(buffer, (palette) -> this.palette.serializeForNetwork(buffer, runtimeMapper));
    }

    private synchronized void serialize(ByteBuf buffer, IOConsumer<MCWorldBlockPalette> paletteSerializer) {
        int bitsPerBlock = Math.max((int) Math.ceil(Math.log(this.palette.getEntries().size()) / Math.log(2)), 1);
        int blocksPerWord = 32 / bitsPerBlock;
        int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord);

        buffer.writeByte((bitsPerBlock << 1) | 1);

        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = 0;
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) {
                    break;
                }

                word |= this.blocks[pos] << (bitsPerBlock * block);
                pos++;
            }
            buffer.writeIntLE(word);
        }

        paletteSerializer.accept(this.palette);
    }

    /**
     * Remove palette entries that are no longer used in this chunk.
     */
    private synchronized void cleanUpPalette() {
        // Get all the palette indexes being used
        Map<Integer, BlockPalette.Entry> usedEntries = new HashMap<>();
        for (int paletteIndex : this.blocks) {
            usedEntries.put(paletteIndex, this.palette.getEntry(paletteIndex));
        }

        // Remove unused palette entries
        Iterator<BlockPalette.Entry> entryIterator = new HashSet<>(this.palette.getEntries()).iterator();
        while (entryIterator.hasNext()) {
            BlockPalette.Entry entry = entryIterator.next();
            int paletteIndex = this.palette.getPaletteIndex(entry);

            // Air occupies the first element of the block palette and CANNOT be removed or else empty elements of
            // this.blocks will not resolve to air. Any other palette entry can be removed.
            if (!usedEntries.containsKey(paletteIndex) && !entry.getId().equals(BlockPalette.EmptyEntry.ID)) {
                entryIterator.remove();
                this.palette.removeEntry(entry, false);
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
