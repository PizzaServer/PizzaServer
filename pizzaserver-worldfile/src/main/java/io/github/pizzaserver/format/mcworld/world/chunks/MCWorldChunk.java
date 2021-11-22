package io.github.pizzaserver.format.mcworld.world.chunks;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.format.BlockRuntimeMapper;
import io.github.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.pizzaserver.format.exceptions.world.chunks.ChunkParseException;
import io.github.pizzaserver.format.mcworld.utils.VarInts;
import io.github.pizzaserver.format.mcworld.world.chunks.subchunks.MCWorldSubChunk;
import io.github.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MCWorldChunk implements BedrockChunk {

    private final int x;
    private final int z;
    private final int dimension;
    private final int chunkVersion;

    private final int[] heightMap = new int[256];
    private final byte[] biomeData = new byte[256];
    private List<MCWorldSubChunk> subChunks;

    private final Set<NbtMap> entityNBTs = new HashSet<>();
    private final Set<NbtMap> blockEntityNBTs = new HashSet<>();

    private MCWorldChunk(
            int x,
            int z,
            int dimension,
            int chunkVersion,
            byte[] data2d,
            byte[] blockEntityData,
            byte[] entityData,
            byte[][] subChunks
    ) throws IOException {
        this.chunkVersion = chunkVersion;
        this.x = x;
        this.z = z;
        this.dimension = dimension;

        this.parseData2d(data2d);
        this.parseEntityNBT(blockEntityData, entityData);
        this.parseSubChunks(subChunks);
    }

    /**
     * Parses height maps and biome data of a chunk.
     * @param data2d height + biome data of the chunk
     */
    private void parseData2d(byte[] data2d) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(512);
        buffer.writeBytes(data2d);

        // Ensure data is written to main memory
        synchronized (this.heightMap) {
            for (int i = 0; i < this.heightMap.length; i++) {
                this.heightMap[i] = buffer.readUnsignedShortLE();
            }
            buffer.readBytes(this.biomeData);
            buffer.release();
        }
    }

    /**
     * Parses block entities and regular entities of a chunk.
     * @param blockEntityData block entities in this chunk
     * @param entityData entities in this chunk
     * @throws IOException if it failed to parse the entity NBT
     */
    private void parseEntityNBT(byte[] blockEntityData, byte[] entityData) throws IOException {
        try (InputStream blockEntityDataStream = new ByteArrayInputStream(blockEntityData);
                NBTInputStream blockEntityNBTInputStream = NbtUtils.createReaderLE(blockEntityDataStream)) {
            while (blockEntityDataStream.available() > 0) {
                NbtMap compound = (NbtMap) blockEntityNBTInputStream.readTag();
                this.blockEntityNBTs.add(compound);
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to read block entity NBT", exception);
        }
        try (InputStream entityDataStream = new ByteArrayInputStream(entityData);
                NBTInputStream entityNBTInputStream = NbtUtils.createReaderLE(entityDataStream)) {
            while (entityDataStream.available() > 0) {
                NbtMap compound = (NbtMap) entityNBTInputStream.readTag();
                this.entityNBTs.add(compound);
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to read entity NBT", exception);
        }
    }

    private void parseSubChunks(byte[][] subChunks) throws IOException {
        this.subChunks = new ArrayList<>();
        for (byte[] subChunkBytes : subChunks) {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(subChunkBytes.length);
            try {
                buffer.writeBytes(subChunkBytes);

                MCWorldSubChunk subChunk = new MCWorldSubChunk(buffer);
                this.subChunks.add(subChunk);
            } finally {
                buffer.release();
            }
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

    @Override
    public int getDimension() {
        return this.dimension;
    }

    public int getChunkVersion() {
        return this.chunkVersion;
    }

    public Set<NbtMap> getEntityNBTs() {
        return this.entityNBTs;
    }

    public Set<NbtMap> getBlockEntityNBTs() {
        return this.blockEntityNBTs;
    }

    @Override
    public synchronized int[] getHeightMap() {
        return this.heightMap;
    }

    @Override
    public int getHighestBlockAt(Vector2i position) {
        return this.getHighestBlockAt(position.getX(), position.getY());
    }

    @Override
    public synchronized int getHighestBlockAt(int x, int y) {
        return this.heightMap[y * 16 + x];
    }

    @Override
    public void setHighestBlockAt(Vector2i position, int newHeight) {
        this.setHighestBlockAt(position.getX(), position.getY(), newHeight);
    }

    @Override
    public synchronized void setHighestBlockAt(int x, int z, int newHeight) {
        this.heightMap[z * 16 + x] = newHeight;
    }

    @Override
    public synchronized byte[] getBiomeData() {
        return this.biomeData;
    }

    @Override
    public byte getBiomeAt(Vector2i position) {
        return this.getBiomeAt(position.getX(), position.getY());
    }

    @Override
    public synchronized byte getBiomeAt(int x, int z) {
        return this.biomeData[z * 16 + x];
    }

    @Override
    public void setBiomeAt(Vector2i position, byte biome) {
        this.setBiomeAt(position.getX(), position.getY(), biome);
    }

    @Override
    public synchronized void setBiomeAt(int x, int z, byte biome) {
        this.biomeData[z * 16 + x] = biome;
    }

    @Override
    public int getSubChunkCount() {
        int subChunkCount = this.getSubChunks().size() - 1;
        for (; subChunkCount >= 0; subChunkCount--) {
            BedrockSubChunk subChunk = this.getSubChunks().get(subChunkCount);
            if (subChunk.getLayers().size() > 0) {
                break;
            }
        }

        return subChunkCount + 1;
    }

    @Override
    public List<BedrockSubChunk> getSubChunks() {
        return Collections.unmodifiableList(this.subChunks);
    }

    @Override
    public BedrockSubChunk getSubChunk(int index) {
        if (index < this.subChunks.size()) {
            return this.subChunks.get(index);
        } else {
            return null;
        }
    }

    @Override
    public byte[] serializeForDisk() {
        throw new UnsupportedOperationException("Cannot serialize MCWorldChunk for disk. Serialize subchunks individually instead");
    }

    @Override
    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) throws IOException {
        int subChunkCount = this.getSubChunkCount();

        // Write all subchunks
        ByteBuf packetData = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] data;
        try {
            for (int subChunkIndex = 0; subChunkIndex < subChunkCount; subChunkIndex++) {
                BedrockSubChunk subChunk = this.getSubChunks().get(subChunkIndex);
                packetData.writeBytes(subChunk.serializeForNetwork(runtimeMapper));
            }
            packetData.writeBytes(this.getBiomeData());
            packetData.writeByte(0);    // edu feature or smth
            VarInts.writeUnsignedInt(packetData, 0);    // border blocks supposedly

            data = new byte[packetData.readableBytes()];
            packetData.readBytes(data);
        } finally {
            packetData.release();
        }

        return data;
    }


    public static class Builder {

        private int x;
        private int z;
        private int dimension;

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

        public Builder setDimension(int dimension) {
            this.dimension = dimension;
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
                    Check.nullParam(this.dimension, "dimension"),
                    this.chunkVersion,
                    Check.nullParam(this.heightAndBiomeData, "heightAndBiomeData"),
                    Check.nullParam(this.blockEntityData, "blockEntityData"),
                    Check.nullParam(this.entityData, "entityData"),
                    Check.nullParam(this.subChunks, "subChunks")
            );
        }

    }

}
