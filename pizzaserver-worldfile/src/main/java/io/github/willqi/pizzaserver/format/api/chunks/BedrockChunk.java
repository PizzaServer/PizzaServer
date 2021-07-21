package io.github.willqi.pizzaserver.format.api.chunks;

import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;

import java.util.List;

public interface BedrockChunk {

    int getX();

    int getZ();

    int[] getHeightMap();

    int getHeightAt(Vector2i position);

    int getHeightAt(int x, int z);

    byte[] getBiomeData();

    byte getBiomeAt(Vector2i position);

    byte getBiomeAt(int x, int z);

    void setBiomeAt(Vector2i position, byte biome);

    void setBiomeAt(int x, int z, byte biome);

    List<BedrockSubChunk> getSubChunks();

    BedrockSubChunk getSubChunk(int index);

}
