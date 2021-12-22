package io.github.pizzaserver.format.api;

import io.github.pizzaserver.format.api.chunks.BedrockChunkProvider;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunk;

import java.io.Closeable;
import java.io.IOException;

public interface BedrockLevel<T extends BedrockChunkProvider<? extends MCWorldChunk>> extends Closeable {

    T getChunkProvider() throws IOException;

    LevelData getLevelData() throws IOException;

    void setLevelData(LevelData levelData) throws IOException;

}
