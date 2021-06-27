package io.github.willqi.pizzaserver.mcworld.world.chunks;

import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8.V8SubChunkVersion;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class BedrockChunk {

    private final int x;
    private final int z;
    private final int chunkVersion;

    private final byte[] heightMap;
    private final byte[] biomeData;
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

        this.x = x;
        this.z = z;

        this.heightMap = new byte[0];
        this.biomeData = new byte[0];

        this.chunkVersion = chunkVersion;

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
