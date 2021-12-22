package io.github.pizzaserver.server.level.providers.leveldb;

import io.github.pizzaserver.server.level.providers.BaseLevelProvider;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.format.api.LevelData;
import io.github.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.pizzaserver.format.mcworld.MCWorldLevel;
import io.github.pizzaserver.format.exceptions.world.chunks.NoChunkFoundException;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunk;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunkProvider;

import java.io.File;
import java.io.IOException;

public class LevelDBLevelProvider extends BaseLevelProvider {

    private final LevelData levelData;
    private final MCWorldChunkProvider chunkProvider;


    public LevelDBLevelProvider(File worldFile) throws IOException {
        super(worldFile);
        MCWorldLevel mcWorldLevel = new MCWorldLevel(worldFile);
        this.levelData = mcWorldLevel.getLevelData();
        this.chunkProvider = mcWorldLevel.getChunkProvider();
    }

    @Override
    public LevelData getLevelData() {
        return this.levelData;
    }

    @Override
    public BedrockChunk getChunk(int x, int z, Dimension dimension) throws IOException {
        MCWorldChunk internalChunk;
        try {
            internalChunk = this.chunkProvider.getChunk(x, z, dimension.ordinal());
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
        this.chunkProvider.close();
    }
}
