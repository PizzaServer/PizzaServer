package io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8;

import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockSubChunk;
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
    public BedrockSubChunk parse(ByteBuf buffer) {

        int chunkLayers = buffer.readByte(); // Verison 8 allows up to 256 layers. I'm assuming this is for things like waterlogging.

        for (int layer = 0; layer < chunkLayers; layer++) {
            int blockSize = buffer.readByte() >> 1;
            boolean padded = blockSize == 3 || blockSize == 5 || blockSize == 6;
            int intsToStoreBlocks = (4096 / (32 / blockSize)) + (padded ? 1 : 0);

            int[] blocks = new int[intsToStoreBlocks];
            for (int blockI = 0; blockI < blocks.length; blockI++) {
                blocks[blockI] = buffer.readIntLE();
                System.out.println(blocks[blockI]);
            }

            int paletteLength = buffer.readIntLE();

            NBTInputStream inputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteBufInputStream(buffer)));
            try {
                for (int i = 0; i < paletteLength; i++) {
                    NBTCompound compound = inputStream.readCompound();
                    System.out.println(compound.getString("name").getValue() + " " + compound.getInteger("version").getValue());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return new BedrockSubChunk();
    }
}
