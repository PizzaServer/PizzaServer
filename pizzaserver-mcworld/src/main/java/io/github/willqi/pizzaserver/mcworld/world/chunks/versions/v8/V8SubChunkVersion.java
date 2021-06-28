package io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.mcworld.exceptions.world.chunks.ChunkParseException;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.SubChunkVersion;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;


public class V8SubChunkVersion extends SubChunkVersion {

    public static final SubChunkVersion INSTANCE = new V8SubChunkVersion();


    // Dragonfly and Prismarine were a huge help with figuring out how Bedrock chunks can be read.
    @Override
    public BedrockSubChunk parse(ByteBuf buffer) throws IOException {

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
            buffer.setIndex(chunkBlocksReaderIndex, buffer.writerIndex());
            BedrockSubChunk.RawBlock[] blocks = this.getBlocks(buffer, intsToStoreBlocks, blocksPerWord, blockSize);

            layers[layer] = new BedrockSubChunk.Layer(blocks, palette);
            // Go back to the end of the palette to prepare for the next layer
            buffer.setIndex(endPaletteIndex, buffer.writerIndex());
        }

        return new BedrockSubChunk(layers);
    }

    @Override
    public byte[] serialize(BedrockSubChunk subChunk) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        int chunkLayers = subChunk.getLayers().length;
        buffer.writeByte(chunkLayers);
        for (int layerIndex = 0; layerIndex < chunkLayers; layerIndex++) {
            BedrockSubChunk.Layer layer = subChunk.getLayer(layerIndex);

            // TODO: remove unused palette pieces
            int blockSize = (int)Math.floor(Math.log(subChunk.getLayer(layerIndex).getPalette().getAllPaletteData().size()) / Math.log(2)) + 1;
            int blocksPerWord = 32 / blockSize;
            boolean padded = blockSize == 3 || blockSize == 5 || blockSize == 6;
            int intsToStoreBlocks = (4096 / blocksPerWord) + (padded ? 1 : 0);

            buffer.writeByte((blockSize << 1) | 1);

            byte[] serializedBlocks = this.serializeBlocks(layer.getBlocks(), intsToStoreBlocks, blocksPerWord, blockSize);
            buffer.writeBytes(serializedBlocks);

            byte[] serializedPalette = this.serializeBlockPalette(layer.getPalette());
            buffer.writeBytes(serializedPalette);
        }

        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }

    protected BedrockSubChunk.RawBlock[] getBlocks(ByteBuf buffer, int intsToStoreBlocks, int blocksPerWord, int blockSize) {
        // https://github.com/JSPrismarine/JSPrismarine/blob/df616663ae436475e0939326641270a8e04b7e3f/packages/prismarine/src/world/chunk/BlockStorage.ts#L108
        // Useful for determining how to parse the chunk blocks.
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

        return blocks;
    }

    protected byte[] serializeBlocks(BlockPalette.BlockPaletteData[] blocks, int intsToStoreBlocks, int blocksPerWord, int blockSize) {
        // https://github.com/JSPrismarine/JSPrismarine/blob/df616663ae436475e0939326641270a8e04b7e3f/packages/prismarine/src/world/chunk/BlockStorage.ts#L71
        // Useful for determining how to write the chunk blocks.
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        int chunkBlockIndex = 0;

        for (int blockIntI = 0; blockIntI < intsToStoreBlocks; blockIntI++) {
            int word = 0;
            for (int blockI = 0; blockI < blocksPerWord; blockI++) {
                word |= blocks[chunkBlockIndex].getId() << (blockSize * blockI);
                chunkBlockIndex++;
            }
            buffer.writeIntLE(word);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
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

    protected byte[] serializeBlockPalette(BlockPalette palette) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeIntLE(palette.getAllPaletteData().size());
        NBTOutputStream outputStream = new NBTOutputStream(new LittleEndianDataOutputStream(new ByteBufOutputStream(buffer)));
        for (BlockPalette.BlockPaletteData data : palette.getAllPaletteData()) {
            NBTCompound compound = new NBTCompound();
            compound.put("name", new NBTString("name", data.getName()))
                    .put("version", new NBTInteger("version", data.getVersion()))
                    .put("states", data.getState());

            outputStream.writeCompound(compound);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }

}
