package io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8;

import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.SubChunkVersion;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class V8SubChunkVersion extends SubChunkVersion {

    public static final SubChunkVersion INSTANCE = new V8SubChunkVersion();


    @Override
    public BedrockSubChunk parse(ByteBuf buffer) {

        int chunkLayers = buffer.readByte(); // Verison 8 allows up to 256 layers. I'm assuming this is for things like waterlogging.

        for (int layer = 0; layer < chunkLayers; layer++) {
            int blockSize = buffer.readByte() >> 1;
            int blocksPerInt = 32 / blockSize;
            int intsToStoreBlocks = 4096 / blocksPerInt;

            int[] blocks = new int[intsToStoreBlocks];
            for (int blockI = 0; blockI < blocks.length; blockI++) {
                blocks[blockI] = buffer.readIntLE();
            }

            int paletteLength = buffer.readIntLE();
            buffer.skipBytes(4);    // Most likely a varint that has the byte length

            NBTInputStream inputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteBufInputStream(buffer)));
            try {
                for (int i = 0; i < paletteLength; i++) {
                    NBTCompound compound = inputStream.readCompound();
                    System.out.println(compound.getString("name").getValue());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return new BedrockSubChunk();
    }
}
