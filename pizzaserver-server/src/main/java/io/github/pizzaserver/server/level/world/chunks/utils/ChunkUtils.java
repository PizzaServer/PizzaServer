package io.github.pizzaserver.server.level.world.chunks.utils;

import io.github.pizzaserver.format.api.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.BedrockSubChunk;

import java.io.IOException;

public class ChunkUtils {

    private ChunkUtils() {}

    public static int getSubChunkCount(BedrockChunk chunk) throws IOException {
        int subChunkCount = 20;
        for (; subChunkCount >= -4; subChunkCount--) {
            BedrockSubChunk subChunk = chunk.getSubChunk(subChunkCount);
            if (subChunk.getLayers().size() > 0) {
                break;
            }
        }

        return subChunkCount + 5;   // 1 + (4 negative sub chunks)
    }

}
