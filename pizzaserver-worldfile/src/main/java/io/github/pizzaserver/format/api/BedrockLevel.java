package io.github.pizzaserver.format.api;

import io.github.pizzaserver.format.api.chunks.BedrockChunkProvider;
import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunk;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public interface BedrockLevel<T extends BedrockChunkProvider<? extends MCWorldChunk>> extends Closeable {

    T getChunkProvider();

    LevelData getLevelData();

    void setLevelData(LevelData levelData) throws IOException;

    File getFile();

}
