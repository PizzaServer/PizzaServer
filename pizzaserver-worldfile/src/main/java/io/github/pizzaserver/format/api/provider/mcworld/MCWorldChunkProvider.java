package io.github.pizzaserver.format.api.provider.mcworld;

import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.format.api.data.DimensionIds;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockHeightMap;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.api.provider.BedrockChunkProvider;
import io.github.pizzaserver.format.api.provider.mcworld.data.ChunkKey;
import io.github.pizzaserver.format.api.provider.mcworld.data.MCWorldChunkData;
import io.github.pizzaserver.format.api.provider.mcworld.utils.MCWorldFormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.iq80.leveldb.DB;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class MCWorldChunkProvider implements BedrockChunkProvider {

    private static final int CHUNK_VERSION = 27;

    protected final DB database;
    private boolean closed;


    public MCWorldChunkProvider(DB database) {
        this.database = database;
    }

    @Override
    public BedrockChunk getChunk(int dimension, int x, int z) throws IOException {
        if (this.isClosed()) {
            throw new IllegalStateException("Cannot retrieve chunk on closed provider.");
        }
        // Extract chunk version
        byte chunkVersion = this.getChunkVersion(x, z, dimension);

        // Extract height map and biome data
        MCWorldChunkData chunkDataPalette = this.getChunkDataPalette(x, z, dimension);

        // Extract block entities within this chunk
        Set<NbtMap> blockEntities = this.getChunkBlockEntities(x, z, dimension);

        // Extract entities within this chunk
        Set<NbtMap> entities = this.getChunkEntities(x, z, dimension);

        BedrockChunk chunk = new BedrockChunk(this, dimension, x, z);
        chunk.setVersion(chunkVersion);
        chunk.setHeightMap(chunkDataPalette.getHeightMap());
        chunk.setBiomeMap(chunkDataPalette.getBiomeMap());
        chunk.setBlockEntities(blockEntities);
        chunk.setEntities(entities);

        return chunk;
    }

    @Override
    public BedrockSubChunk getSubChunk(int dimension, int x, int z, int subChunkIndex) throws IOException {
        if (this.isClosed()) {
            throw new IllegalStateException("Cannot retrieve chunk on closed provider.");
        }

        byte[] subChunkKey;
        if (dimension == DimensionIds.OVERWORLD) {
            subChunkKey = ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, subChunkIndex);
        } else {
            subChunkKey = ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(x, z, dimension, subChunkIndex);
        }

        byte[] subChunkData = this.database.get(subChunkKey);
        if (subChunkData == null) {
            subChunkData = new byte[]{ 8, 0 };
        }

        ByteBuf buffer = Unpooled.copiedBuffer(subChunkData);

        try {
            return MCWorldFormatUtils.readSubChunk(buffer);
        } finally {
            buffer.release();
        }
    }

    private byte getChunkVersion(int x, int z, int dimension) {
        byte[] versionKey;
        if (dimension == DimensionIds.OVERWORLD) {
            versionKey = ChunkKey.VERSION.getLevelDBKey(x, z);
        } else {
            versionKey = ChunkKey.VERSION.getLevelDBKey(x, z, dimension);
        }

        byte[] versionData = this.database.get(versionKey);
        if (versionData == null) {
            versionData = new byte[] { (byte) CHUNK_VERSION };
            this.database.put(versionKey, versionData);
        }

        return versionData[0];
    }

    private MCWorldChunkData getChunkDataPalette(int x, int z, int dimension) throws IOException {
        byte[] heightAnd3DBiomeKey;
        byte[] heightAnd2DBiomeKey;
        if (dimension == DimensionIds.OVERWORLD) {
            heightAnd2DBiomeKey = ChunkKey.DATA_2D.getLevelDBKey(x, z);
            heightAnd3DBiomeKey = ChunkKey.DATA_3D.getLevelDBKey(x, z);
        } else {
            heightAnd2DBiomeKey = ChunkKey.DATA_2D.getLevelDBKeyWithDimension(x, z, dimension);
            heightAnd3DBiomeKey = ChunkKey.DATA_3D.getLevelDBKeyWithDimension(x, z, dimension);
        }


        byte[] heightAnd3DBiomeData = this.database.get(heightAnd3DBiomeKey);
        byte[] heightAnd2DBiomeData = this.database.get(heightAnd2DBiomeKey);

        // Check for 3D data before 2D data
        MCWorldChunkData chunkData;
        if (heightAnd3DBiomeData == null && heightAnd2DBiomeData == null) {
            // Biome and height map data does not exist.
            chunkData = new MCWorldChunkData(new BedrockHeightMap(), new BedrockBiomeMap());

            byte[] biomeData = MCWorldFormatUtils.biomeMapToBytes(chunkData.getBiomeMap());
            byte[] heightData = MCWorldFormatUtils.heightMapToBytes(chunkData.getHeightMap());

            // Put data together to form complete 3D data
            byte[] data = new byte[biomeData.length + heightData.length];
            System.arraycopy(heightData, 0, data, 0, heightData.length);
            System.arraycopy(biomeData, 0, data, heightData.length, biomeData.length);

            if (dimension == DimensionIds.OVERWORLD) {
                this.database.put(ChunkKey.DATA_3D.getLevelDBKey(x, z), data);
            } else {
                this.database.put(ChunkKey.DATA_3D.getLevelDBKeyWithDimension(x, z, dimension), data);
            }
        } else if (heightAnd3DBiomeData != null) {
            // 3D biome data
            chunkData = MCWorldFormatUtils.read3DChunkData(heightAnd3DBiomeData);
        } else {
            // 2D biome data
            chunkData = MCWorldFormatUtils.read2DChunkData(heightAnd2DBiomeData);
        }

        return chunkData;
    }

    private Set<NbtMap> getChunkBlockEntities(int x, int z, int dimension) throws IOException {
        byte[] blockEntityKey;
        if (dimension == DimensionIds.OVERWORLD) {
            blockEntityKey = ChunkKey.BLOCK_ENTITIES.getLevelDBKey(x, z);
        } else {
            blockEntityKey = ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(x, z, dimension);
        }

        byte[] blockEntityData = this.database.get(blockEntityKey);
        if (blockEntityData == null) {
            blockEntityData = new byte[0];
        }

        Set<NbtMap> blockEntities = new HashSet<>();
        try (InputStream blockEntityDataStream = new ByteArrayInputStream(blockEntityData);
             NBTInputStream blockEntityNBTInputStream = NbtUtils.createReaderLE(blockEntityDataStream)) {
            while (blockEntityDataStream.available() > 0) {
                NbtMap blockEntityNBT = (NbtMap) blockEntityNBTInputStream.readTag();
                blockEntities.add(blockEntityNBT);
            }
        }

        return blockEntities;
    }

    private Set<NbtMap> getChunkEntities(int x, int z, int dimension) throws IOException {
        byte[] entitiesKey;
        if (dimension == DimensionIds.OVERWORLD) {
            entitiesKey = ChunkKey.ENTITIES.getLevelDBKey(x, z);
        } else {
            entitiesKey = ChunkKey.ENTITIES.getLevelDBKeyWithDimension(x, z, dimension);
        }

        byte[] entitiesData = this.database.get(entitiesKey);
        if (entitiesData == null) {
            entitiesData = new byte[0];
        }

        Set<NbtMap> entities = new HashSet<>();
        try (InputStream entityDataStream = new ByteArrayInputStream(entitiesData);
             NBTInputStream entityNBTInputStream = NbtUtils.createReaderLE(entityDataStream)) {
            while (entityDataStream.available() > 0) {
                NbtMap entityNBT = (NbtMap) entityNBTInputStream.readTag();
                entities.add(entityNBT);
            }
        }

        return entities;
    }

    @Override
    public void saveChunk(BedrockChunk bedrockChunk) throws IOException {

    }

    @Override
    public void saveSubChunk(BedrockSubChunk subChunk) throws IOException {

    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("This provider is already closed.");
        }
        this.closed = true;

        this.database.close();
    }

}
