package io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.mcworld.exceptions.world.chunks.ChunkParseException;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.SubChunkVersion;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;


public class V8SubChunkVersion extends SubChunkVersion {

    public static final SubChunkVersion INSTANCE = new V8SubChunkVersion();


    // Dragonfly was a huge help with figuring out how Bedrock chunks can be read.
    @Override
    public BedrockSubChunk parse(ByteBuf buffer) throws ChunkParseException {

        int chunkLayers = buffer.readByte(); // Verison 8 allows up to 256 layers. I'm assuming this is for things like waterlogging.

        BedrockSubChunk.Layer[] layers = new BedrockSubChunk.Layer[chunkLayers];

        for (int layer = 0; layer < chunkLayers; layer++) {
            int blockSize = buffer.readByte() >> 1;
            int blocksPerWord = 32 / blockSize;
            boolean padded = blockSize == 3 || blockSize == 5 || blockSize == 6;
            int intsToStoreBlocks = (4096 / blocksPerWord) + (padded ? 1 : 0);

            // We want to read the palette first so we can translate what blocks are immediately.
            int chunkBlocksReaderIndex = buffer.readerIndex();
            buffer.setIndex(chunkBlocksReaderIndex + (intsToStoreBlocks * 4), buffer.writerIndex());
            BlockPalette palette = this.getBlockPalette(buffer);
            int endPaletteIndex = buffer.readerIndex();

            // Parse the blocks within the chunk.
            // https://github.com/JSPrismarine/JSPrismarine/blob/df616663ae436475e0939326641270a8e04b7e3f/packages/prismarine/src/world/chunk/BlockStorage.ts#L108
            // Useful for determining how to parse the chunk blocks.
            buffer.setIndex(chunkBlocksReaderIndex, buffer.writerIndex());

            BedrockSubChunk.RawBlock[] blocks = new BedrockSubChunk.RawBlock[intsToStoreBlocks * blocksPerWord];
            int chunkBlockIndex = 0;
            for (int blockIntI = 0; blockIntI < intsToStoreBlocks; blockIntI++) {
                int word = buffer.readIntLE();  // This integer can store multiple blocks.
                for (int blockI = 0; blockI < blocksPerWord; blockI++) {
                    int paletteType = (word >> (chunkBlockIndex % blocksPerWord) * blockSize) & ((1 << blockSize) - 1);
                    Vector3i position = new Vector3i(
                            (chunkBlockIndex >> 8) & 0x0f,
                            chunkBlockIndex & 0x0f,
                            (chunkBlockIndex >> 4) & 0x0f
                    );
                    blocks[chunkBlockIndex] = new BedrockSubChunk.RawBlock(paletteType, position);

                    chunkBlockIndex++;
                }
            }

            layers[layer] = new BedrockSubChunk.Layer(blocks, palette);
            // Go back to the end of the palette to prepare for the next layer
            buffer.setIndex(endPaletteIndex, buffer.writerIndex());
        }

        return new BedrockSubChunk(layers);
    }

    protected BlockPalette getBlockPalette(ByteBuf buffer) throws ChunkParseException {
        BlockPalette blockPalette = new BlockPalette();
        int paletteLength = buffer.readIntLE();
        NBTInputStream inputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteBufInputStream(buffer)));
        try {
            for (int i = 0; i < paletteLength; i++) {
                NBTCompound compound = inputStream.readCompound();
                blockPalette.add(compound);
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to parse chunk palette.", exception);
        }

        return blockPalette;
    }

}
