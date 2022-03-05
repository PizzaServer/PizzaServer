package io.github.pizzaserver.format.provider;

import io.github.pizzaserver.format.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunk;

import java.io.Closeable;
import java.io.IOException;

public interface BedrockChunkProvider extends Closeable {

    BedrockChunk getChunk(int dimension, int x, int z) throws IOException;

    BedrockSubChunk getSubChunk(int dimension, int x, int z, int subChunk) throws IOException;

    void saveChunk(BedrockChunk chunk) throws IOException;

    void saveSubChunk(int dimension, int x, int z, int subChunkIndex, BedrockSubChunk subChunk) throws IOException;

    boolean isClosed();

}
