package io.github.willqi.pizzaserver.mcworld.world.chunks;

import io.github.willqi.pizzaserver.mcworld.exceptions.world.chunks.ChunkParseException;
import org.iq80.leveldb.DB;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MCChunkDatabase implements Closeable {

    private final DB database;


    public MCChunkDatabase(DB database) {
        this.database = database;
    }

    public BedrockChunk getChunk(int x, int z) throws IOException {

        // First extract the chunk version
        byte[] versionData = this.database.get(ChunkKey.VERSION.getLevelDBKey(x, z));
        if (versionData == null) {
            versionData = this.database.get(ChunkKey.OLD_VERSION.getLevelDBKey(x, z));
            if (versionData == null) {
                throw new ChunkParseException("Failed to find version for chunk (" + x + ", " + z + ")");
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
        List<byte[]> subChunkList = new ArrayList<>();
        for (int y = 0; y < 16; y++) {
            byte[] subChunk = this.database.get(ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, y));
            if (subChunk == null) {
                break;
            }
            subChunkList.add(subChunk);
        }
        byte[][] subChunks = subChunkList.toArray(new byte[subChunkList.size()][]);

        return new BedrockChunk.Builder()
                .setX(x)
                .setZ(z)
                .setChunkVersion(chunkVersion)
                .setHeightAndBiomeData(heightAndBiomeData)
                .setBlockEntityData(blockEntityData)
                .setEntityData(entityData)
                .setSubChunks(subChunks)
                .build();
    }

    public void saveChunk(BedrockChunk bedrockChunk) {

    }

    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
