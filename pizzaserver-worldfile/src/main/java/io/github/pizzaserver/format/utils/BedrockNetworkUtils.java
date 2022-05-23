package io.github.pizzaserver.format.utils;

import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.commons.utils.NumberUtils;
import io.github.pizzaserver.format.MinecraftSerializationHandler;
import io.github.pizzaserver.format.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.*;
import io.github.pizzaserver.format.dimension.chunks.subchunk.utils.Palette;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.Set;

public class BedrockNetworkUtils {

    private BedrockNetworkUtils() {}

    public static void serializeSubChunk(ByteBuf buffer, BedrockSubChunk subChunk, boolean persistentBlockstates, MinecraftSerializationHandler serializationHandler) {
        buffer.writeByte(8);    // Convert to version 8 regardless of v1, v8 or v9
        buffer.writeByte(subChunk.getLayers().size());
        for (BlockLayer layer : subChunk.getLayers()) {
            try {
                serializeBlockLayer(buffer, layer, persistentBlockstates, serializationHandler);
            } catch (IOException e) {
                throw new RuntimeException("Failed to serialize BlockLayer", e);
            }
        }
    }

    public static void serializeBlockLayer(ByteBuf buffer, BlockLayer blockLayer, boolean persistentBlockstates, MinecraftSerializationHandler serializationHandler) throws IOException {
        int bitsPerBlock = NumberUtils.log2Ceil(blockLayer.getPalette().getEntries().size()) + 1;
        int blocksPerWord = 32 / bitsPerBlock;
        // integral ceiling division
        int wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;

        buffer.writeByte((bitsPerBlock << 1) | (persistentBlockstates ? 0 : 1));

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

        if (persistentBlockstates) {
            serializePersistentBlockPalette(buffer, blockLayer.getPalette(), serializationHandler);
        } else {
            serializeRuntimeBlockPalette(buffer, blockLayer.getPalette(), serializationHandler);
        }
    }

    public static void serializeRuntimeBlockPalette(ByteBuf buffer, Palette<BlockPaletteEntry> palette, MinecraftSerializationHandler serializationHandler) {
        Set<BlockPaletteEntry> entries = palette.getEntries();
        VarInts.writeInt(buffer, entries.size());

        for (BlockPaletteEntry data : entries) {
            int id = serializationHandler.getBlockRuntimeId(data.getId(), data.getState());
            VarInts.writeInt(buffer, id);
        }
    }

    public static void serializePersistentBlockPalette(ByteBuf buffer, Palette<BlockPaletteEntry> palette, MinecraftSerializationHandler serializationHandler) throws IOException {
        Set<BlockPaletteEntry> entries = palette.getEntries();
        VarInts.writeInt(buffer, entries.size());

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
            throw new IOException("Failed to serialize persistent network chunk", exception);
        }
    }

    public static void serialize3DBiomeMap(ByteBuf buffer, BedrockBiomeMap biomeMap, int maxBiomeSubChunkCount) throws IOException {
        BedrockSubChunkBiomeMap lastSubChunkBiomeMap = null;
        for (int biomeSubChunk = 0; biomeSubChunk < maxBiomeSubChunkCount; biomeSubChunk++) {
            if (biomeMap.getSubChunks().size() <= biomeSubChunk) {
                break;
            }

            BedrockSubChunkBiomeMap subChunkBiomeMap = biomeMap.getSubChunk(biomeSubChunk);

            int bitsPerBlock = NumberUtils.log2Ceil(subChunkBiomeMap.getPalette().size()) + 1;
            int blocksPerWord = 0;
            int wordsPerChunk = 0;

            boolean shouldCopyPreviousBiomeSubChunk = subChunkBiomeMap.equals(lastSubChunkBiomeMap)
                    || (lastSubChunkBiomeMap != null && subChunkBiomeMap.getPalette().size() == 0);
            if (shouldCopyPreviousBiomeSubChunk) {
                buffer.writeByte(-1);
                continue;
            }

            if (subChunkBiomeMap.getPalette().getEntries().size() == 0) {
                throw new IOException("biome sub chunk has no biomes present");
            }

            if (bitsPerBlock > 0) {
                blocksPerWord = 32 / bitsPerBlock;
                // integral ceiling division
                wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;
            }

            buffer.writeByte(bitsPerBlock << 1);

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

            VarInts.writeInt(buffer, subChunkBiomeMap.getPalette().getEntries().size());
            for (int i = 0; i < subChunkBiomeMap.getPalette().getEntries().size(); i++) {
                VarInts.writeInt(buffer, subChunkBiomeMap.getPalette().getEntry(i));
            }

            lastSubChunkBiomeMap = subChunkBiomeMap;
        }
    }

}