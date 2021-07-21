package io.github.willqi.pizzaserver.format.mcworld.world.chunks;

import io.github.willqi.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.iq80.leveldb.DB;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class MCChunkDatabase implements Closeable {

    private final DB database;


    public MCChunkDatabase(DB database) {
        this.database = database;
    }

    public MCWorldChunk getChunk(int x, int z) throws IOException {
        // First extract the chunk version
        byte[] versionData = this.database.get(ChunkKey.VERSION.getLevelDBKey(x, z));
        if (versionData == null) {
            versionData = this.database.get(ChunkKey.OLD_VERSION.getLevelDBKey(x, z));
            if (versionData == null) {
                throw new NoChunkFoundException("Could not find a chunk at (" + x + ", " + z + ")");
            }
        }
        int chunkVersion = versionData[0];

        // Extract height map and biome data
        byte[] heightAndBiomeData = this.database.get(ChunkKey.DATA_2D.getLevelDBKey(x, z));

        // Extract block entities within this chunk
        byte[] blockEntityData = this.database.get(ChunkKey.BLOCK_ENTITIES.getLevelDBKey(x, z));
        if (blockEntityData == null) {
            blockEntityData = new byte[0];  // Not all chunks will have block entities.
        }

        // Extract entities within this chunk
        byte[] entityData = this.database.get(ChunkKey.ENTITIES.getLevelDBKey(x, z));
        if (entityData == null) {
            entityData = new byte[0];   // Not all chunks have entities.
        }

        // Extract subchunks
        byte[][] subChunks = new byte[16][];
        for (int y = 0; y < 16; y++) {
            byte[] subChunk = this.database.get(ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, y));
            if (subChunk == null) {
                subChunks[y] = new byte[]{8, 0};    // empty v8 chunk
            } else {
                subChunks[y] = subChunk;
            }
        }

        return new MCWorldChunk.Builder()
                .setX(x)
                .setZ(z)
                .setChunkVersion(chunkVersion)
                .setHeightAndBiomeData(heightAndBiomeData)
                .setBlockEntityData(blockEntityData)
                .setEntityData(entityData)
                .setSubChunks(subChunks)
                .build();
    }

    public void saveChunk(MCWorldChunk bedrockChunk) throws IOException {
        this.database.put(ChunkKey.VERSION.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()), new byte[]{ (byte)bedrockChunk.getChunkVersion() });

        // Data2D (height map + biome)
        ByteBuf heightAndBiomeBuffer = ByteBufAllocator.DEFAULT.buffer(768, 768);    // 512 + 256
        for (int height : bedrockChunk.getHeightMap()) {
            heightAndBiomeBuffer.writeShortLE(height);
        }
        heightAndBiomeBuffer.writeBytes(bedrockChunk.getBiomeData());

        byte[] heightAndBiomeData = new byte[768];
        heightAndBiomeBuffer.readBytes(heightAndBiomeBuffer);
        heightAndBiomeBuffer.release();
        this.database.put(ChunkKey.DATA_2D.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()), heightAndBiomeData);

        // block entities
        ByteArrayOutputStream blockEntityOutput = new ByteArrayOutputStream();
        NBTOutputStream blockEntityNBTOutputStream = new NBTOutputStream(new LittleEndianDataOutputStream(blockEntityOutput));
        for (NBTCompound blockEntityCompound : bedrockChunk.getBlockEntityNBTs()) {
            blockEntityNBTOutputStream.writeCompound(blockEntityCompound);
        }
        this.database.put(ChunkKey.BLOCK_ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()), blockEntityOutput.toByteArray());

        // entities
        ByteArrayOutputStream entityOutput = new ByteArrayOutputStream();
        NBTOutputStream entityNBTOutputStream = new NBTOutputStream(new LittleEndianDataOutputStream(entityOutput));
        for (NBTCompound entityCompound : bedrockChunk.getEntityNBTs()) {
            entityNBTOutputStream.writeCompound(entityCompound);
        }
        this.database.put(ChunkKey.ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()), entityOutput.toByteArray());

        // sub chunks
        for (int y = 0; y < 16; y++) {
            byte[] subChunk = bedrockChunk.getSubChunk(y).serializeForDisk();
            this.database.put(ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ(), y), subChunk);
        }

    }

    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
