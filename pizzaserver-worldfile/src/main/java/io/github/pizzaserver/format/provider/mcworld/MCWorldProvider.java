package io.github.pizzaserver.format.provider.mcworld;

import org.cloudburstmc.math.vector.Vector3i;
import io.github.pizzaserver.format.data.LevelData;
import io.github.pizzaserver.format.data.DimensionIds;
import io.github.pizzaserver.format.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.dimension.chunks.BedrockHeightMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.provider.BedrockProvider;
import io.github.pizzaserver.format.provider.mcworld.data.ChunkKey;
import io.github.pizzaserver.format.provider.mcworld.data.MCWorldChunkData;
import io.github.pizzaserver.format.provider.mcworld.utils.MCWorldFormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.iq80.leveldb.DB;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MCWorldProvider implements BedrockProvider {

    private static final int CHUNK_VERSION = 27;

    protected File levelFile;
    protected final DB database;
    private boolean closed;


    public MCWorldProvider(File levelFile, DB database) {
        this.levelFile = levelFile;
        this.database = database;
    }

    @Override
    public BedrockChunk getChunk(int dimension, int x, int z) throws IOException {
        if (this.isClosed()) {
            throw new IllegalStateException("Cannot retrieve chunk on closed provider.");
        }
        // Extract chunk version
        byte chunkVersion = this.getChunkVersion(dimension, x, z);

        // Extract height map and biome data
        MCWorldChunkData chunkDataPalette = this.getChunkData(dimension, x, z);

        // Extract block entities within this chunk
        Set<NbtMap> blockEntities = this.getBlockEntities(dimension, x, z);

        // Extract entities within this chunk
        Set<NbtMap> entities = this.getEntities(dimension, x, z);

        BedrockChunk chunk = new BedrockChunk(this, dimension, x, z);
        chunk.setVersion(chunkVersion);
        chunk.setHeightMap(chunkDataPalette.getHeightMap());
        chunk.setBiomeMap(chunkDataPalette.getBiomeMap());

        for (NbtMap blockEntityNBT : blockEntities) {
            chunk.addBlockEntity(blockEntityNBT);
        }

        for (NbtMap entityNBT : entities) {
            chunk.addEntity(entityNBT);
        }

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

        ByteBuf buffer = Unpooled.wrappedBuffer(subChunkData);
        buffer.readerIndex(0);

        try {
            return MCWorldFormatUtils.readSubChunk(buffer);
        } finally {
            buffer.release();
        }
    }

    private byte getChunkVersion(int dimension, int x, int z) {
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

    private void saveChunkVersion(int x, int z, int dimension, byte version) {
        byte[] versionKey;
        if (dimension == DimensionIds.OVERWORLD) {
            versionKey = ChunkKey.VERSION.getLevelDBKey(x, z);
        } else {
            versionKey = ChunkKey.VERSION.getLevelDBKey(x, z, dimension);
        }

        this.database.put(versionKey, new byte[]{ version });
    }

    private MCWorldChunkData getChunkData(int dimension, int x, int z) throws IOException {
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

            this.database.put(heightAnd3DBiomeKey, data);
        } else if (heightAnd3DBiomeData != null) {
            // 3D biome data
            chunkData = MCWorldFormatUtils.read3DChunkData(heightAnd3DBiomeData);
        } else {
            // 2D biome data
            chunkData = MCWorldFormatUtils.read2DChunkData(heightAnd2DBiomeData);
        }

        return chunkData;
    }

    private void saveData(int dimension, int x, int z, MCWorldChunkData chunkData) throws IOException {
        byte[] heightAnd3DBiomeKey;
        if (dimension == DimensionIds.OVERWORLD) {
            heightAnd3DBiomeKey = ChunkKey.DATA_3D.getLevelDBKey(x, z);
        } else {
            heightAnd3DBiomeKey = ChunkKey.DATA_3D.getLevelDBKeyWithDimension(x, z, dimension);
        }

        byte[] biomeData = MCWorldFormatUtils.biomeMapToBytes(chunkData.getBiomeMap());
        byte[] heightData = MCWorldFormatUtils.heightMapToBytes(chunkData.getHeightMap());

        // Put data together to form complete 3D data
        byte[] data = new byte[biomeData.length + heightData.length];
        System.arraycopy(heightData, 0, data, 0, heightData.length);
        System.arraycopy(biomeData, 0, data, heightData.length, biomeData.length);

        this.database.put(heightAnd3DBiomeKey, data);
    }

    private Set<NbtMap> getBlockEntities(int dimension, int x, int z) throws IOException {
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

    private void saveBlockEntities(int dimension, int x, int z, Map<Vector3i, NbtMap> blockEntities) throws IOException {
        byte[] blockEntityKey;
        if (dimension == DimensionIds.OVERWORLD) {
            blockEntityKey = ChunkKey.BLOCK_ENTITIES.getLevelDBKey(x, z);
        } else {
            blockEntityKey = ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(x, z, dimension);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                NBTOutputStream blockEntityNBTOutputStream = NbtUtils.createWriterLE(outputStream)) {
            // Write block entities nbt
            for (NbtMap blockEntity : blockEntities.values()) {
                blockEntityNBTOutputStream.writeTag(blockEntity);
            }

            byte[] data = outputStream.toByteArray();
            this.database.put(blockEntityKey, data);
        }
    }

    private Set<NbtMap> getEntities(int dimension, int x, int z) throws IOException {
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

    private void saveEntities(int dimension, int x, int z, Set<NbtMap> entities) throws IOException {
        byte[] entitiesKey;
        if (dimension == DimensionIds.OVERWORLD) {
            entitiesKey = ChunkKey.ENTITIES.getLevelDBKey(x, z);
        } else {
            entitiesKey = ChunkKey.ENTITIES.getLevelDBKeyWithDimension(x, z, dimension);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             NBTOutputStream entityNBTOutputStream = NbtUtils.createWriterLE(outputStream)) {
            // Write block entities nbt
            for (NbtMap entity : entities) {
                entityNBTOutputStream.writeTag(entity);
            }

            byte[] data = outputStream.toByteArray();
            this.database.put(entitiesKey, data);
        }
    }

    @Override
    public void saveChunk(BedrockChunk bedrockChunk) throws IOException {
        if (this.isClosed()) {
            throw new IllegalStateException("Cannot retrieve chunk on closed provider.");
        }

        // Save chunk data
        this.saveChunkVersion(bedrockChunk.getDimension(), bedrockChunk.getX(), bedrockChunk.getZ(), bedrockChunk.getVersion());
        this.saveData(bedrockChunk.getDimension(), bedrockChunk.getX(), bedrockChunk.getZ(), new MCWorldChunkData(bedrockChunk.getHeightMap(), bedrockChunk.getBiomeMap()));
        this.saveBlockEntities(bedrockChunk.getDimension(), bedrockChunk.getX(), bedrockChunk.getZ(), bedrockChunk.getBlockEntities());
        this.saveEntities(bedrockChunk.getDimension(), bedrockChunk.getX(), bedrockChunk.getZ(), bedrockChunk.getEntities());

        // Save every subchunk
        for (int subChunkIndex = -4; subChunkIndex < 20; subChunkIndex++) {
            BedrockSubChunk subChunk = bedrockChunk.getSubChunk(subChunkIndex);
            this.saveSubChunk(bedrockChunk.getDimension(), bedrockChunk.getX(), bedrockChunk.getZ(), subChunkIndex, subChunk);
        }
    }

    @Override
    public void saveSubChunk(int dimension, int x, int z, int subChunkIndex, BedrockSubChunk subChunk) throws IOException {
        if (this.isClosed()) {
            throw new IllegalStateException("Cannot retrieve chunk on closed provider.");
        }

        byte[] subChunkKey;
        if (dimension == DimensionIds.OVERWORLD) {
            subChunkKey = ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, subChunkIndex);
        } else {
            subChunkKey = ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(x, z, dimension, subChunkIndex);
        }

        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            MCWorldFormatUtils.writeSubChunk(buffer, subChunk);

            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);

            this.database.put(subChunkKey, data);
        } finally {
            buffer.release();
        }
    }

    @Override
    public LevelData getLevelData() throws IOException {
        return MCWorldFormatUtils.readLevelData(this.levelFile);
    }

    @Override
    public void saveLevelData(LevelData data) throws IOException {
        MCWorldFormatUtils.writeLevelData(this.levelFile, data);
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
