package io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8;

import io.github.willqi.pizzaserver.mcworld.exceptions.world.chunks.ChunkParseException;
import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.BlockPalette;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.SubChunkVersion;
import io.netty.buffer.ByteBuf;


public class V8SubChunkVersion extends SubChunkVersion {

    public static final SubChunkVersion INSTANCE = new V8SubChunkVersion();


    // Dragonfly was a huge help with figuring out how Bedrock chunks can be read.
    @Override
    public BedrockSubChunk parse(ByteBuf buffer) throws ChunkParseException {

        int chunkLayers = buffer.readByte(); // Verison 8 allows up to 256 layers. I'm assuming this is for things like waterlogging.

        for (int layer = 0; layer < chunkLayers; layer++) {
            int blockSize = buffer.readByte() >> 1;
            int blocksPerWord = 32 / blockSize;
            boolean padded = blockSize == 3 || blockSize == 5 || blockSize == 6;
            int intsToStoreBlocks = (4096 / blocksPerWord) + (padded ? 1 : 0);

            // We want to read the palette first so we can translate what blocks are immediately.
            int chunkBlocksReaderIndex = buffer.readerIndex();
            buffer.setIndex(chunkBlocksReaderIndex + (intsToStoreBlocks * 4), buffer.writerIndex());

            BlockPalette blockPalette = new BlockPalette(buffer);
            int endPaletteIndex = buffer.readerIndex();

            // Parse the blocks within the chunk.
            // https://github.com/JSPrismarine/JSPrismarine/blob/df616663ae436475e0939326641270a8e04b7e3f/packages/prismarine/src/world/chunk/BlockStorage.ts#L108
            // Useful for determining how to parse the chunk blocks.
            buffer.setIndex(chunkBlocksReaderIndex, buffer.writerIndex());
            int[] blocks = new int[intsToStoreBlocks * blocksPerWord];
            int chunkBlockIndex = 0;
            for (int blockIntI = 0; blockIntI < intsToStoreBlocks; blockIntI++) {
                int word = buffer.readIntLE();  // This integer can store multiple blocks.
                for (int blockI = 0; blockI < blocksPerWord; blockI++) {
                    int paletteType = (word >> (chunkBlockIndex % blocksPerWord) * blockSize) & ((1 << blockSize) - 1);
                    blocks[chunkBlockIndex] = paletteType;
                    chunkBlockIndex++;
                }
            }

            buffer.setIndex(endPaletteIndex, buffer.writerIndex());
        }

        return new BedrockSubChunk();
    }
}
