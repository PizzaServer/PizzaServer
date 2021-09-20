package io.github.willqi.pizzaserver.format.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MCWorldSubChunk implements BedrockSubChunk {

    private final List<BlockLayer> layers = new ArrayList<>();
    private int subChunkVersion;


    public int getSubChunkVersion() {
        return this.subChunkVersion;
    }

    @Override
    public BlockLayer getLayer(int layer) {
        while (layer >= this.getLayers().size()) {
            MCWorldBlockPalette blockPalette = new MCWorldBlockPalette();
            blockPalette.add(new BlockPalette.EmptyEntry());    // ensure the palette has air

            BlockLayer blockLayer = new MCWorldBlockLayer(blockPalette);
            this.addLayer(blockLayer);
        }
        return this.layers.get(layer);
    }

    @Override
    public List<BlockLayer> getLayers() {
        return this.layers;
    }

    @Override
    public void addLayer(BlockLayer layer) {
        this.layers.add(layer);
    }

    @Override
    public byte[] serializeForDisk() throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1);
        buffer.writeByte(8);    // Convert to version 8 regardless of v1 or v8
        buffer.writeByte(this.layers.size());
        for (BlockLayer layer : this.layers) {
            buffer.writeBytes(layer.serializeForDisk());
        }

        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();
        return serialized;
    }

    @Override
    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1);
        buffer.writeByte(8);    // Convert to version 8 regardless of v1 or v8
        buffer.writeByte(this.layers.size());
        for (BlockLayer layer : this.layers) {
            buffer.writeBytes(layer.serializeForNetwork(runtimeMapper));
        }

        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();
        return serialized;
    }

    /**
     * Construct a sub chunk based on data retrieved from the level database.
     * @param buffer buffer to parse
     * @throws IOException if parsing failed
     */
    public void parse(ByteBuf buffer) throws IOException {
        this.subChunkVersion = buffer.readByte();
        int totalLayers;

        switch (this.subChunkVersion) {
            case 8:
                totalLayers = buffer.readByte();
                break;
            case 1:
                totalLayers = 1;
                break;
            default:
                throw new UnsupportedEncodingException("Missing implementation for v" + this.subChunkVersion + " chunks");
        }

        for (int layerI = 0; layerI < totalLayers; layerI++) {
            int bitsPerBlock = buffer.readByte() >> 1;
            int blocksPerWord = 32 / bitsPerBlock;
            int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord);

            // We want to read the palette first so we can translate what blocks are immediately.
            int chunkBlocksIndex = buffer.readerIndex();
            buffer.setIndex(chunkBlocksIndex + (wordsPerChunk * 4), buffer.writerIndex());

            MCWorldBlockPalette palette = new MCWorldBlockPalette();
            palette.parse(buffer);
            int endPaletteIndex = buffer.readerIndex(); // we jump to this index after reading the blocks

            // Go back and parse the blocks.
            buffer.setIndex(chunkBlocksIndex, buffer.writerIndex());
            MCWorldBlockLayer layer = new MCWorldBlockLayer(palette);
            layer.parse(buffer, bitsPerBlock, blocksPerWord, wordsPerChunk);
            this.layers.add(layer);

            // Go back to the end of the palette to prepare for the next layer
            buffer.setIndex(endPaletteIndex, buffer.writerIndex());
        }
    }

}
