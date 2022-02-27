package io.github.pizzaserver.format.api.provider.mcworld.utils;

import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockHeightMap;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.*;
import io.github.pizzaserver.format.api.chunks.ChunkParseException;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.utils.Palette;
import io.github.pizzaserver.format.api.provider.mcworld.data.MCWorldChunkData;
import io.netty.buffer.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MCWorldFormatUtils {

    private MCWorldFormatUtils() {}

    public static BedrockSubChunk readSubChunk(ByteBuf buffer) throws IOException {
        BedrockSubChunk subChunk = new BedrockSubChunk();
        int subChunkVersion = buffer.readByte();

        int amountOfLayers;
        switch (subChunkVersion) {
            case 9:
                amountOfLayers = buffer.readByte();
                buffer.readByte();  // data driven heights
                break;
            case 8:
                amountOfLayers = buffer.readByte();
                break;
            case 1:
                amountOfLayers = 1;
                break;
            default:
                throw new ChunkParseException("Unknown sub chunk version: v" + subChunkVersion);
        }

        for (int layerI = 0; layerI < amountOfLayers; layerI++) {
            BlockLayer layer = readLayer(buffer);
            subChunk.addLayer(layer);
        }

        return subChunk;
    }

    public static void writeSubChunk(ByteBuf buffer, BedrockSubChunk subChunk) throws IOException {
        buffer.writeByte(8);
        buffer.writeByte(subChunk.getLayers().size());

        List<BlockLayer> layers = subChunk.getLayers();
        for (BlockLayer layer : layers) {
            writeLayer(buffer, layer);
        }
    }

    public static BlockLayer readLayer(ByteBuf buffer) throws IOException {
        int bitsPerBlock = buffer.readByte() >> 1;
        int blocksPerWord = 32 / bitsPerBlock;
        int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord); // there are 4096 blocks in a chunk stored in x words

        // We want to read the palette first so that we can translate what blocks are immediately.
        int chunkBlocksIndex = buffer.readerIndex();
        buffer.setIndex(chunkBlocksIndex + (wordsPerChunk * 4), buffer.writerIndex());

        Palette<BlockPaletteEntry> palette = readPalette(buffer);
        int endPaletteIndex = buffer.readerIndex(); // we jump to this index after reading the blocks

        // Go back and parse the blocks.
        buffer.setIndex(chunkBlocksIndex, buffer.writerIndex());
        BlockLayer layer = new BlockLayer(palette);

        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = buffer.readIntLE();  // This integer can store multiple minecraft blocks.
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) {
                    break;
                }

                int paletteIndex = (word >> (pos % blocksPerWord) * bitsPerBlock) & ((1 << bitsPerBlock) - 1);
                layer.setBlockEntryAt(pos >> 8, pos & 15, (pos >> 4) & 15, palette.getEntry(paletteIndex));

                pos++;
            }
        }

        // Go back to the end of the palette to prepare for the next layer
        buffer.setIndex(endPaletteIndex, buffer.writerIndex());

        return layer;
    }

    public static void writeLayer(ByteBuf buffer, BlockLayer layer) throws IOException {
        layer.resize();

        int bitsPerBlock = Math.max((int) Math.ceil(Math.log(layer.getPalette().getEntries().size()) / Math.log(2)), 1);
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

                word |= layer.getPalette().getPaletteIndex(layer.getBlockEntryAt(pos >> 8, (pos >> 4) & 15, pos & 15)) << (bitsPerBlock * block);
                pos++;
            }
            buffer.writeIntLE(word);
        }

        writePalette(buffer, layer.getPalette());
    }

    public static Palette<BlockPaletteEntry> readPalette(ByteBuf buffer) throws IOException {
        Palette<BlockPaletteEntry> palette = new Palette<>();

        int paletteLength = buffer.readIntLE();
        try (NBTInputStream inputStream = NbtUtils.createReaderLE(new ByteBufInputStream(buffer))) {
            for (int i = 0; i < paletteLength; i++) {
                NbtMap compound = (NbtMap) inputStream.readTag();

                String blockId = compound.getString("name");
                int version = compound.getInt("version");
                NbtMap states = compound.getCompound("states");

                palette.addEntry(new BlockPaletteEntry(blockId, version, states));
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to parse chunk palette.", exception);
        }

        return palette;
    }

    public static void writePalette(ByteBuf buffer, Palette<BlockPaletteEntry> palette) throws IOException {
        Set<BlockPaletteEntry> entries = palette.getEntries();
        buffer.writeIntLE(entries.size());
        try (NBTOutputStream outputStream = NbtUtils.createWriterLE(new ByteBufOutputStream(buffer))) {
            for (BlockPaletteEntry data : entries) {
                NbtMap compound = NbtMap.builder()
                        .putString("name", data.getId())
                        .putInt("version", data.getVersion())
                        .putCompound("states", data.getState())
                        .build();

                outputStream.writeTag(compound);
            }
        } catch (IOException exception) {
            throw new IOException("Failed to serialize chunk to disk", exception);
        }
    }

    public static MCWorldChunkData read2DChunkData(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);

        try {
            // Parse height map
            BedrockHeightMap heightMap = new BedrockHeightMap();
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;
                heightMap.setHighestBlockAt(x, z, buffer.readUnsignedShortLE());
            }

            // Parse biome map
            BedrockBiomeMap biomeMap = new BedrockBiomeMap();

            byte[] biomeData = new byte[256];
            buffer.readBytes(biomeData);

            // Begin constructing 3D biomes...
            BedrockSubChunkBiomeMap[] subChunkBiomeMaps = new BedrockSubChunkBiomeMap[25];
            for (int i = 0; i < 25; i++) {
                subChunkBiomeMaps[i] = new BedrockSubChunkBiomeMap(new Palette<>());
            }

            // Assign every value in a column to its biome.
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;

                for (int subChunkIndex = 0; subChunkIndex < 25; subChunkIndex++) {
                    for (int y = 0; y < 16; y++) {
                        subChunkBiomeMaps[subChunkIndex].setBiomeAt(x, y, z, biomeData[i]);
                    }
                }
            }

            // construct the biome map.
            for (int i = 0; i < 25; i++) {
                biomeMap.setSubChunk(i, subChunkBiomeMaps[i]);
            }

            return new MCWorldChunkData(heightMap, biomeMap);
        } finally {
            buffer.release();
        }
    }

    public static MCWorldChunkData read3DChunkData(byte[] data) throws IOException {
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        buffer.readerIndex(0);

        try {
            // Parse height map
            BedrockHeightMap heightMap = new BedrockHeightMap();
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;
                heightMap.setHighestBlockAt(x, z, buffer.readUnsignedShortLE());
            }

            // Parse biome map
            BedrockBiomeMap biomeMap = new BedrockBiomeMap();
            BedrockSubChunkBiomeMap lastBiomeSubChunk = null;

            int subChunkIndex = 0;
            while (buffer.readableBytes() > 0) {
                int bitsPerBlock = buffer.readByte() >> 1;

                // if the bits is -1, that means that we should just copy the last biome map.
                if (bitsPerBlock == -1) {
                    if (lastBiomeSubChunk == null) {
                        throw new ChunkParseException("Cannot use last biome subchunk if none exists.");
                    }
                    biomeMap.setSubChunk(subChunkIndex++, lastBiomeSubChunk.clone());
                    continue;
                }

                // because the palette is written after the data, we keep a mental note of where the biome data is
                // so that we can construct the palette first.
                int biomeDataIndex = 0;
                int paletteLength = 1;  // for biomes, we assume by default that there is at least 1 biome present

                if (bitsPerBlock > 0) {
                    int blocksPerWord = 32 / bitsPerBlock;
                    int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord);

                    biomeDataIndex = buffer.readerIndex();

                    buffer.skipBytes(wordsPerChunk * 4);
                    paletteLength = buffer.readIntLE();
                }

                // Parse biome palette
                Palette<Integer> palette = new Palette<>();
                for (int i = 0; i < paletteLength; i++) {
                    palette.addEntry(buffer.readIntLE());
                }

                int endOfPaletteIndex = buffer.readerIndex();

                // Begin constructing the biome map for this subchunk
                BedrockSubChunkBiomeMap subChunkBiomeMap = new BedrockSubChunkBiomeMap(palette);
                if (bitsPerBlock > 0) {
                    // Move our index back to the biome data before the palette
                    buffer.readerIndex(biomeDataIndex);

                    int blocksPerWord = 32 / bitsPerBlock;
                    int wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord);

                    int pos = 0;
                    for (int i = 0; i < wordsPerChunk; i++) {
                        int word = buffer.readIntLE();  // stores multiple biomes in 1 int
                        for (int block = 0; block < blocksPerWord; block++) {
                            if (pos >= 4096) {
                                break;
                            }

                            // Break apart the word into coordinates for each block's biome in the subchunk
                            int paletteIndex = (word >> (pos % blocksPerWord) * bitsPerBlock) & ((1 << bitsPerBlock) - 1);
                            subChunkBiomeMap.setBiomeAt(pos >> 8, pos & 15, (pos >> 4) & 15, palette.getEntry(paletteIndex));

                            pos++;
                        }
                    }
                }

                buffer.readerIndex(endOfPaletteIndex);

                // Add the biome subchunk to our biome map.
                lastBiomeSubChunk = subChunkBiomeMap;
                biomeMap.setSubChunk(subChunkIndex++, lastBiomeSubChunk);
            }

            return new MCWorldChunkData(heightMap, biomeMap);
        } finally {
            buffer.release();
        }
    }

    public static byte[] heightMapToBytes(BedrockHeightMap heightMap) {
        byte[] data = new byte[512];
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        buffer.writerIndex(0);

        try {
            for (int height : heightMap.array()) {
                buffer.writeShortLE(height);
            }

            return data;
        } finally {
            buffer.release();
        }
    }

    public static byte[] biomeMapToBytes(BedrockBiomeMap biomeMap) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            BedrockSubChunkBiomeMap lastSubChunkBiomeMap = null;
            for (BedrockSubChunkBiomeMap subChunkBiomeMap : biomeMap.getSubChunks()) {
                if (subChunkBiomeMap.getPalette().getEntries().size() == 0) {
                    throw new IOException("biome sub chunk has no biomes present");
                }

                int bitsPerBlock = (int) Math.ceil(Math.log(subChunkBiomeMap.getPalette().getEntries().size()) / Math.log(2));
                int blocksPerWord = 0;
                int wordsPerChunk = 0;

                if (subChunkBiomeMap.equals(lastSubChunkBiomeMap)) {
                    buffer.writeByte(-1);
                    continue;
                }


                if (bitsPerBlock > 0) {
                    blocksPerWord = 32 / bitsPerBlock;
                    wordsPerChunk = (int) Math.ceil(4096d / blocksPerWord);
                }

                buffer.writeByte((bitsPerBlock << 1) | 1);

                int pos = 0;
                for (int i = 0; i < wordsPerChunk; i++) {
                    int word = 0;
                    for (int block = 0; block < blocksPerWord; block++) {
                        if (pos >= 4096) {
                            break;
                        }

                        word |= subChunkBiomeMap.getPalette().getPaletteIndex(subChunkBiomeMap.getBiomeAt(pos >> 8, (pos >> 4) & 15, pos & 15)) << (bitsPerBlock * block);
                        pos++;
                    }
                    buffer.writeIntLE(word);
                }

                if (bitsPerBlock > 0) {
                    buffer.writeIntLE(subChunkBiomeMap.getPalette().getEntries().size());
                }
                for (int i = 0; i < subChunkBiomeMap.getPalette().getEntries().size(); i++) {
                    buffer.writeIntLE(subChunkBiomeMap.getPalette().getEntry(i));
                }

                lastSubChunkBiomeMap = subChunkBiomeMap;
            }

            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);

            return data;
        } finally {
            buffer.release();
        }
    }


}
