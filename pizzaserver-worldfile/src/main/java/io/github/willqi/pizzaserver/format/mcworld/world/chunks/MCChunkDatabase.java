package io.github.willqi.pizzaserver.format.mcworld.world.chunks;

import io.github.willqi.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import org.iq80.leveldb.DB;

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

    public void saveChunk(MCWorldChunk bedrockChunk) {

    }

    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
