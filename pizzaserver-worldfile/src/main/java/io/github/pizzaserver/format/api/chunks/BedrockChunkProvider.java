package io.github.pizzaserver.format.api.chunks;

import java.io.Closeable;
import java.io.IOException;

public interface BedrockChunkProvider<T extends BedrockChunk> extends Closeable {

    T getChunk(int x, int z, int dimension) throws IOException;

    void saveChunk(T chunk) throws IOException;

}
