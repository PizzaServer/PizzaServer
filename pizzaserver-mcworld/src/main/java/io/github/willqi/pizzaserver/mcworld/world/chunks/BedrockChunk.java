package io.github.willqi.pizzaserver.mcworld.world.chunks;

import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8.V8SubChunkVersion;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BedrockChunk {

    private final int x;
    private final int z;
    private final int chunkVersion;

    private final int[] heightMap = new int[256];
    private final byte[] biomeData = new byte[256];
    private BedrockSubChunk[] subChunks;

    private final Set<NBTCompound> entityNBTs = new HashSet<>();
    private final Set<NBTCompound> blockEntityNBTs = new HashSet<>();

    private BedrockChunk (
            int x,
            int z,
            int chunkVersion,
            byte[] heightAndBiomeData,
            byte[] blockEntityData,
            byte[] entityData,
            byte[][] subChunks
    ) throws IOException {
        this.chunkVersion = chunkVersion;
        this.x = x;
        this.z = z;

        ByteBuf heightAndBiomeBuf = ByteBufAllocator.DEFAULT.buffer(512);
        heightAndBiomeBuf.writeBytes(heightAndBiomeData);
        for (int i = 0; i < this.heightMap.length; i++) {
            this.heightMap[i] = heightAndBiomeBuf.readUnsignedShortLE();
        }
        heightAndBiomeBuf.readBytes(this.biomeData);
        heightAndBiomeBuf.release();

        NBTInputStream blockEntityNBTInputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteArrayInputStream(blockEntityData)));
        while (blockEntityNBTInputStream.available() > 0) {
            this.blockEntityNBTs.add(blockEntityNBTInputStream.readCompound());
        }

        NBTInputStream entityNBTInputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteArrayInputStream(entityData)));
        while (entityNBTInputStream.available() > 0) {
            this.entityNBTs.add(entityNBTInputStream.readCompound());
        }


        this.subChunks = new BedrockSubChunk[subChunks.length];
        for (int i = 0; i < this.subChunks.length; i++) {
            byte[] subChunkBytes = subChunks[i];

            ByteBuf subChunkBuffer = ByteBufAllocator.DEFAULT.buffer(subChunkBytes.length);
            subChunkBuffer.writeBytes(subChunkBytes);
            int subChunkVersion = subChunkBuffer.readByte();

            BedrockSubChunk subChunk;
            switch (subChunkVersion) {
                case 8:
                    subChunk = V8SubChunkVersion.INSTANCE.parse(subChunkBuffer);
                    break;
                default:
                    throw new UnsupportedOperationException("Cannot parse sub chunk data because there is no sub chunk version handler for v" + subChunkBytes);
            }

            subChunkBuffer.release();
            this.subChunks[i] = subChunk;
        }

    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public int getChunkVersion() {
        return this.chunkVersion;
    }

    public Set<NBTCompound> getEntityNBTs() {
        return this.entityNBTs;
    }

    public Set<NBTCompound> getBlockEntityNBTs() {
        return this.blockEntityNBTs;
    }

    public int[] getHeightMap() {
        return this.heightMap;
    }

    public int getHeightAt(Vector2i position) {
        return this.getHeightAt(position.getX(), position.getY());
    }

    public int getHeightAt(int x, int y) {
        return this.heightMap[y * 16 + x];
    }

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    public byte getBiomeAt(Vector2i position) {
        return this.getBiomeAt(position.getX(), position.getY());
    }

    public byte getBiomeAt(int x, int y) {
        return this.biomeData[y * 16 + x];
    }

    public BedrockSubChunk getSubChunk(int index) {
        if (index < this.subChunks.length) {
            return this.subChunks[index];
        } else {
            return null;
        }
    }


    public static class Builder {

        private int x;
        private int z;

        private int chunkVersion;

        private byte[] heightAndBiomeData = new byte[0];
        private byte[][] subChunks = new byte[0][];

        private byte[] blockEntityData = new byte[0];
        private byte[] entityData = new byte[0];


        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setZ(int z) {
            this.z = z;
            return this;
        }

        public Builder setChunkVersion(int chunkVersion) {
            this.chunkVersion = chunkVersion;
            return this;
        }

        public Builder setHeightAndBiomeData(byte[] heightAndBiomeData) {
            this.heightAndBiomeData = heightAndBiomeData;
            return this;
        }

        public Builder setBlockEntityData(byte[] blockEntityData) {
            this.blockEntityData = blockEntityData;
            return this;
        }

        public Builder setEntityData(byte[] entityData) {
            this.entityData = entityData;
            return this;
        }

        public Builder setSubChunks(byte[][] subChunks) {
            this.subChunks = subChunks;
            return this;
        }

        public BedrockChunk build() throws IOException {
            return new BedrockChunk(
                    this.x,
                    this.z,
                    this.chunkVersion,
                    this.heightAndBiomeData,
                    this.blockEntityData,
                    this.entityData,
                    this.subChunks
            );
        }

    }

}
