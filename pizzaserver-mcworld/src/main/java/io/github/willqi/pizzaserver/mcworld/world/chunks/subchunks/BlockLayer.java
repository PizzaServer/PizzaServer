package io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.mcworld.BlockRuntimeMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.daporkchop.lib.common.function.io.IOFunction;

public class BlockLayer {

    private final BlockPalette palette;
    private final RawBlock[] blocks = new RawBlock[4096];


    public BlockLayer(BlockPalette palette) {
        this.palette = palette;
    }

    public void parse(ByteBuf buffer, int bitsPerBlock, int blocksPerWord, int wordsPerChunk) {
        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = buffer.readIntLE();  // This integer can store multiple blocks.
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) break;

                int paletteIndex = (word >> (pos % blocksPerWord) * bitsPerBlock) & ((1 << bitsPerBlock) - 1);
                Vector3i blockPosition = new Vector3i(
                        (pos >> 8) & 0x0f,
                        pos & 0x0f,
                        (pos >> 4) & 0x0f
                );
                this.blocks[pos] = new RawBlock(palette.getEntry(paletteIndex), blockPosition);
                pos++;
            }
        }
    }

    public byte[] serializeForDisk() {
        return this.serialize(BlockPalette::serializeForDisk);
    }

    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) {
        return this.serialize(palette -> this.palette.serializeForNetwork(runtimeMapper));
    }

    protected byte[] serialize(IOFunction<BlockPalette, byte[]> paletteSerializer) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        int bitsPerBlock = (int)Math.ceil(Math.log(this.palette.getAllEntries().size()) / Math.log(2));
        int blocksPerWord = 32 / bitsPerBlock;
        int wordsPerChunk = (int)Math.ceil(4096d / blocksPerWord);

        buffer.writeByte((bitsPerBlock << 1) | 1);

        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = 0;
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) break;
                word |= (this.blocks[pos].getPaletteEntry().getPaletteIndex()) << (bitsPerBlock * block);
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
     * Represents the raw data retrieved from parsing the sub chunk data. (paletteId and it's position)
     */
    public static class RawBlock {

        private final BlockPalette.Entry paletteEntry;
        private final Vector3i position;


        public RawBlock(BlockPalette.Entry entry, Vector3i position) {
            this.paletteEntry = entry;
            this.position = position;
        }

        public BlockPalette.Entry getPaletteEntry() {
            return this.paletteEntry;
        }

        public Vector3i getPosition() {
            return this.position;
        }

    }

}
