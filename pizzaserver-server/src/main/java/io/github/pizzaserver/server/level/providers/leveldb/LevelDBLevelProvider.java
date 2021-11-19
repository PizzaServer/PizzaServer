package io.github.pizzaserver.server.level.providers.leveldb;

import io.github.pizzaserver.server.level.providers.BaseLevelProvider;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.format.api.LevelData;
import io.github.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.pizzaserver.format.mcworld.MCWorld;
import io.github.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunk;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunkProvider;
import io.github.pizzaserver.format.mcworld.world.info.MCWorldInfo;

import java.io.File;
import java.io.IOException;

public class LevelDBLevelProvider extends BaseLevelProvider {

    private final MCWorld mcWorld;
    private final MCWorldInfo worldInfo;
    private final MCWorldChunkProvider chunkDatabase;


    public LevelDBLevelProvider(File worldFile) throws IOException {
        super(worldFile);
        this.mcWorld = new MCWorld(worldFile);
        this.worldInfo = this.mcWorld.getWorldInfo();
        this.chunkDatabase = this.mcWorld.openChunkProvider();
    }

    @Override
    public LevelData getLevelData() {
        return this.worldInfo;
    }

    @Override
    public BedrockChunk getChunk(int x, int z, Dimension dimension) throws IOException {
        MCWorldChunk internalChunk;
        try {
            internalChunk = this.chunkDatabase.getChunk(x, z, dimension.ordinal());
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
                    .setDimension(dimension.ordinal())
                    .build();
        }
        return internalChunk;
    }

    @Override
    public void close() throws IOException {
        this.chunkDatabase.close();
    }
}
