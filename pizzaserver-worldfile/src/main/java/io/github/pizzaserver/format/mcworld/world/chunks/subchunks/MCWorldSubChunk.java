package io.github.pizzaserver.format.mcworld.world.chunks.subchunks;

import io.github.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.pizzaserver.format.MinecraftDataMapper;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MCWorldSubChunk implements BedrockSubChunk {

    private final List<BlockLayer> layers = new ArrayList<>();
    private int subChunkVersion;


    /**
     * Parse a sub chunk based off of the subchunk data retrieved from an .mcworld file
     * @param buffer sub chunk data
     * @throws IOException if it failed to read the subchunk data
     */
    public MCWorldSubChunk(ByteBuf buffer) throws IOException {
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
                throw new UnsupportedOperationException("Missing implementation for v" + this.subChunkVersion + " chunks");
        }

        for (int layerI = 0; layerI < totalLayers; layerI++) {
            int bitsPerBlock = buffer.readByte() >> 1;
            int blocksPerWord = 32 / bitsPerBlock;
            int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord); // there are 4096 blocks in a chunk stored in x words

            // We want to read the palette first so that we can translate what blocks are immediately.
            int chunkBlocksIndex = buffer.readerIndex();
            buffer.setIndex(chunkBlocksIndex + (wordsPerChunk * 4), buffer.writerIndex());

            MCWorldBlockPalette palette = new MCWorldBlockPalette(buffer);
            int endPaletteIndex = buffer.readerIndex(); // we jump to this index after reading the blocks

            // Go back and parse the blocks.
            buffer.setIndex(chunkBlocksIndex, buffer.writerIndex());
            MCWorldBlockLayer layer = new MCWorldBlockLayer(palette, buffer, bitsPerBlock, blocksPerWord, wordsPerChunk);
            synchronized (this.layers) {
                this.layers.add(layer);
            }

            // Go back to the end of the palette to prepare for the next layer
            buffer.setIndex(endPaletteIndex, buffer.writerIndex());
        }
    }

    public int getSubChunkVersion() {
        return this.subChunkVersion;
    }

    @Override
    public BlockLayer getLayer(int layer) {
        synchronized (this.layers) {
            while (layer >= this.getLayers().size()) {
                MCWorldBlockPalette blockPalette = new MCWorldBlockPalette();
                blockPalette.add(new BlockPalette.EmptyEntry());    // ensure the palette has air

                BlockLayer blockLayer = new MCWorldBlockLayer(blockPalette);
                this.addLayer(blockLayer);
            }
            return this.layers.get(layer);
        }
    }

    @Override
    public synchronized List<BlockLayer> getLayers() {
        return this.layers;
    }

    @Override
    public synchronized void addLayer(BlockLayer layer) {
        this.layers.add(layer);
    }

    @Override
    public synchronized void serializeForDisk(ByteBuf buffer) throws IOException {
        buffer.writeByte(8);    // Convert to version 8 regardless of v1 or v8
        buffer.writeByte(this.layers.size());
        for (BlockLayer layer : this.layers) {
            layer.serializeForDisk(buffer);
        }
    }

    @Override
    public synchronized void serializeForNetwork(ByteBuf buffer, MinecraftDataMapper runtimeMapper) throws IOException {
        buffer.writeByte(8);    // Convert to version 8 regardless of v1 or v8
        buffer.writeByte(this.layers.size());
        for (BlockLayer layer : this.layers) {
            layer.serializeForNetwork(buffer, runtimeMapper);
        }
    }

}
