package io.github.pizzaserver.format.utils;

import io.github.pizzaserver.format.MinecraftSerializationHandler;
import io.github.pizzaserver.format.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.*;
import io.github.pizzaserver.format.dimension.chunks.subchunk.utils.Palette;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Set;

public class BedrockNetworkUtils {

    private BedrockNetworkUtils() {}

    public static void serializeSubChunk(ByteBuf buffer, BedrockSubChunk subChunk, MinecraftSerializationHandler serializationHandler) {
        buffer.writeByte(8);    // Convert to version 8 regardless of v1 or v8
        buffer.writeByte(subChunk.getLayers().size());
        for (BlockLayer layer : subChunk.getLayers()) {
            serializeBlockLayer(buffer, layer, serializationHandler);
        }
    }

    public static void serializeBlockLayer(ByteBuf buffer, BlockLayer blockLayer, MinecraftSerializationHandler serializationHandler) {
        int bitsPerBlock = Math.max((int) Math.ceil(Math.log(blockLayer.getPalette().getEntries().size()) / Math.log(2)), 1);
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

                word |= blockLayer.getPalette().getPaletteIndex(blockLayer.getBlockEntryAt(pos >> 8, pos & 15, (pos >> 4) & 15)) << (bitsPerBlock * block);
                pos++;
            }
            buffer.writeIntLE(word);
        }

        serializeBlockPalette(buffer, blockLayer.getPalette(), serializationHandler);
    }

    public static void serializeBlockPalette(ByteBuf buffer, Palette<BlockPaletteEntry> palette, MinecraftSerializationHandler serializationHandler) {
        Set<BlockPaletteEntry> entries = palette.getEntries();
        VarInts.writeInt(buffer, entries.size());

        for (BlockPaletteEntry data : entries) {
            int id = serializationHandler.getBlockRuntimeId(data.getId(), data.getState());
            VarInts.writeInt(buffer, id);
        }
    }

    public static void serialize2DBiomeMap(ByteBuf buffer, BedrockSubChunkBiomeMap biomeMap) {
        for (int i = 0; i < 256; i++) {
            int z = i >> 4;
            int x = i & 15;

            buffer.writeByte(biomeMap.getBiomeAt(x, 0, z));
        }
    }

    public static void serialize3DBiomeMap(ByteBuf buffer, BedrockBiomeMap biomeMap) throws IOException {
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

                    word |= subChunkBiomeMap.getPalette().getPaletteIndex(subChunkBiomeMap.getBiomeAt(pos >> 8, pos & 15, (pos >> 4) & 15)) << (bitsPerBlock * block);
                    pos++;
                }
                buffer.writeIntLE(word);
            }

            if (bitsPerBlock > 0) {
                VarInts.writeInt(buffer, subChunkBiomeMap.getPalette().getEntries().size());
            }

            for (int i = 0; i < subChunkBiomeMap.getPalette().getEntries().size(); i++) {
                VarInts.writeInt(buffer, subChunkBiomeMap.getPalette().getEntry(i));
            }

            lastSubChunkBiomeMap = subChunkBiomeMap;
        }
    }

}
