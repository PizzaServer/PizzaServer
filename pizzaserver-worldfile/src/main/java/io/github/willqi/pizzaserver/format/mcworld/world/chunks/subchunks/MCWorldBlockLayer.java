package io.github.willqi.pizzaserver.format.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.daporkchop.lib.common.function.io.IOFunction;

import java.util.*;

public class MCWorldBlockLayer implements BlockLayer {

    private final MCWorldBlockPalette palette;
    private final int[] blocks = new int[4096];


    public MCWorldBlockLayer(MCWorldBlockPalette palette) {
        this.palette = palette;
    }

    @Override
    public BlockPalette getPalette() {
        return this.palette;
    }

    @Override
    public BlockPalette.Entry getBlockEntryAt(int x, int y, int z) {
        if (this.palette.size() == 0) {
            // if the palette is empty, then add an air entry in order to make this.blocks accurately return air for all 0s.
            this.palette.add(new BlockPalette.EmptyEntry());
        }

        return this.palette.getEntry(this.blocks[this.getBlockIndex(x, y, z)]);
    }

    @Override
    public void setBlockEntryAt(int x, int y, int z, BlockPalette.Entry entry) {
        if (this.palette.size() == 0) {
            // If the palette is empty, then add an air entry to make every 0 in this.blocks return air.
            // Otherwise, when this method calls this.palette.add(entry), every 0 in this.blocks will be assigned that block.
            this.palette.add(new BlockPalette.EmptyEntry());
        }

        this.palette.add(entry);
        this.blocks[this.getBlockIndex(x, y, z)] = this.palette.getPaletteIndex(entry);
    }

    private int getBlockIndex(int x, int y, int z) {
        return (x << 8) | (z << 4) | y;
    }

    public void parse(ByteBuf buffer, int bitsPerBlock, int blocksPerWord, int wordsPerChunk) {
        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = buffer.readIntLE();  // This integer can store multiple blocks.
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
    public byte[] serializeForDisk() {
        this.cleanUpPalette();
        return this.serialize(MCWorldBlockPalette::serializeForDisk);
    }

    @Override
    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) {
        return this.serialize(palette -> this.palette.serializeForNetwork(runtimeMapper));
    }

    private byte[] serialize(IOFunction<MCWorldBlockPalette, byte[]> paletteSerializer) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        int bitsPerBlock = Math.max((int) Math.ceil(Math.log(this.palette.getAllEntries().size()) / Math.log(2)), 1);
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

        buffer.writeBytes(paletteSerializer.apply(this.palette));

        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();
        return serialized;
    }

    /**
     * Remove palette entries that are no longer used in this chunk.
     */
    private void cleanUpPalette() {
        // Get all the palette indexes being used
        Map<Integer, BlockPalette.Entry> usedEntries = new HashMap<>();
        for (int paletteIndex : this.blocks) {
            usedEntries.put(paletteIndex, this.palette.getEntry(paletteIndex));
        }

        // Remove unused palette entries
        Iterator<BlockPalette.Entry> entryIterator = new HashSet<>(this.palette.getAllEntries()).iterator();
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

}
