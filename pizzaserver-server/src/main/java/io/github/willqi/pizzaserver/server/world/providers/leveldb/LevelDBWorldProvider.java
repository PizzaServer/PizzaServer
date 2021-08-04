package io.github.willqi.pizzaserver.server.world.providers.leveldb;

import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.format.api.LevelData;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.format.mcworld.MCWorld;
import io.github.willqi.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import io.github.willqi.pizzaserver.format.mcworld.world.chunks.MCWorldChunk;
import io.github.willqi.pizzaserver.format.mcworld.world.chunks.MCChunkDatabase;
import io.github.willqi.pizzaserver.format.mcworld.world.info.MCWorldInfo;
import io.github.willqi.pizzaserver.server.world.providers.BaseWorldProvider;

import java.io.File;
import java.io.IOException;

public class LevelDBWorldProvider extends BaseWorldProvider {

    private final MCWorld mcWorld;
    private final MCWorldInfo worldInfo;
    private final MCChunkDatabase chunkDatabase;

    public LevelDBWorldProvider(File worldFile) throws IOException {
        this.mcWorld = new MCWorld(worldFile);
        this.worldInfo = this.mcWorld.getWorldInfo();
        this.chunkDatabase = this.mcWorld.openChunkDatabase();
    }

    @Override
    public LevelData getLevelData() {
        return this.worldInfo;
    }

    @Override
    public BedrockChunk getChunk(int x, int z) throws IOException {
        MCWorldChunk internalChunk;
        try {
            internalChunk = this.chunkDatabase.getChunk(x, z);
        } catch (NoChunkFoundException exception) {
            // Empty chunk. Create the empty chunk data.
            byte[][] subChunks = new byte[16][];
            for (int i = 0; i < subChunks.length; i++) {
                subChunks[i] = new byte[]{ 8, 0 };
            }

            internalChunk = new MCWorldChunk.Builder()
                    .setX(x)
                    .setZ(z)
                    .setSubChunks(subChunks)
                    .build();
        }
        return internalChunk;
    }

    @Override
    public void close() throws IOException {
        this.chunkDatabase.close();
    }
}
