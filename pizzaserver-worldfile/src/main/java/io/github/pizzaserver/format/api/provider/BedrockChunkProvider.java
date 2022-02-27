package io.github.pizzaserver.format.api.provider;

import io.github.pizzaserver.format.api.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.BedrockSubChunk;

import java.io.Closeable;
import java.io.IOException;

public interface BedrockChunkProvider extends Closeable {

    BedrockChunk getChunk(int dimension, int x, int z) throws IOException;

    BedrockSubChunk getSubChunk(int dimension, int x, int z, int subChunk) throws IOException;

    void saveChunk(BedrockChunk chunk) throws IOException;

    void saveSubChunk(BedrockSubChunk subChunk) throws IOException;

    boolean isClosed();

}
