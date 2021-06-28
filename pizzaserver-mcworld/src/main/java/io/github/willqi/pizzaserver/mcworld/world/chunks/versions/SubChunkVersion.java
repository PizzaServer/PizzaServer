package io.github.willqi.pizzaserver.mcworld.world.chunks.versions;

import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.versions.v8.V8SubChunkVersion;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class SubChunkVersion {

    protected SubChunkVersion() {}

    public abstract BedrockSubChunk parse(ByteBuf buffer) throws IOException;

    public abstract byte[] serialize(BedrockSubChunk subChunk) throws IOException;

    public static SubChunkVersion getSubChunkVersion(int version) {
        switch (version) {
            case 8:
                return V8SubChunkVersion.INSTANCE;
            default:
                throw new UnsupportedOperationException("Cannot parse sub chunk data because there is no sub chunk version handler for v" + version);
        }
    }

}
