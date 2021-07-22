package io.github.willqi.pizzaserver.format.mcworld.world.chunks;

import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.format.mcworld.world.chunks.subchunks.MCWorldSubChunk;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class MCWorldChunk implements BedrockChunk {

    private final int x;
    private final int z;
    private final int chunkVersion;

    private final int[] heightMap = new int[256];
    private final byte[] biomeData = new byte[256];
    private List<MCWorldSubChunk> subChunks;

    private final Set<NBTCompound> entityNBTs = new HashSet<>();
    private final Set<NBTCompound> blockEntityNBTs = new HashSet<>();

    private MCWorldChunk(
            int x,
            int z,
            int chunkVersion,
            byte[] data2d,
            byte[] blockEntityData,
            byte[] entityData,
            byte[][] subChunks
    ) throws IOException {
        this.chunkVersion = chunkVersion;
        this.x = x;
        this.z = z;

        this.parseData2d(data2d);
        this.parseEntityNBT(blockEntityData, entityData);
        this.parseSubChunks(subChunks);
    }

    /**
     * Parses height maps and biome data of a chunk
     * @param data2d
     */
    private void parseData2d(byte[] data2d) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(512);
        buffer.writeBytes(data2d);

        for (int i = 0; i < this.heightMap.length; i++) {
            this.heightMap[i] = buffer.readUnsignedShortLE();
        }
        buffer.readBytes(this.biomeData);
        buffer.release();
    }

    /**
     * Parses block entities and regular entities of a chunk
     * @param blockEntityData
     * @param entityData
     * @throws IOException
     */
    private void parseEntityNBT(byte[] blockEntityData, byte[] entityData) throws IOException {
        NBTInputStream blockEntityNBTInputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteArrayInputStream(blockEntityData)));
        while (blockEntityNBTInputStream.available() > 0) {
            this.blockEntityNBTs.add(blockEntityNBTInputStream.readCompound());
        }

        NBTInputStream entityNBTInputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteArrayInputStream(entityData)));
        while (entityNBTInputStream.available() > 0) {
            this.entityNBTs.add(entityNBTInputStream.readCompound());
        }
    }

    private void parseSubChunks(byte[][] subChunks) throws IOException {
        this.subChunks = new ArrayList<>(subChunks.length);
        for (int i = 0; i < subChunks.length; i++) {
            byte[] subChunkBytes = subChunks[i];
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(subChunkBytes.length);
            buffer.writeBytes(subChunkBytes);

            MCWorldSubChunk subChunk = new MCWorldSubChunk();
            subChunk.parse(buffer);
            buffer.release();
            this.subChunks.add(subChunk);
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
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

    @Override
    public int[] getHeightMap() {
        return this.heightMap;
    }

    @Override
    public int getHeightAt(Vector2i position) {
        return this.getHeightAt(position.getX(), position.getY());
    }

    @Override
    public int getHeightAt(int x, int y) {
        return this.heightMap[y * 16 + x];
    }

    @Override
    public byte[] getBiomeData() {
        return this.biomeData;
    }

    @Override
    public byte getBiomeAt(Vector2i position) {
        return this.getBiomeAt(position.getX(), position.getY());
    }

    @Override
    public byte getBiomeAt(int x, int z) {
        return this.biomeData[z * 16 + x];
    }

    @Override
    public void setBiomeAt(Vector2i position, byte biome) {
        this.setBiomeAt(position.getX(), position.getY(), biome);
    }

    @Override
    public void setBiomeAt(int x, int z, byte biome) {
        this.biomeData[z * 16 + x] = biome;
    }

    @Override
    public List<BedrockSubChunk> getSubChunks() {
        return Collections.unmodifiableList(this.subChunks);
    }

    public BedrockSubChunk getSubChunk(int index) {
        if (index < this.subChunks.size()) {
            return this.subChunks.get(index);
        } else {
            return null;
        }
    }


    public static class Builder {

        private int x;
        private int z;

        private int chunkVersion;

        private byte[] heightAndBiomeData = new byte[768];
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

        public MCWorldChunk build() throws IOException {
            return new MCWorldChunk(
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
