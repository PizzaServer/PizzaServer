package io.github.willqi.pizzaserver.mcworld.world.chunks.versions;

import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class SubChunkVersion {

    protected SubChunkVersion() {}

    public abstract BedrockSubChunk parse(ByteBuf buffer) throws IOException;

    public abstract byte[] serialize(BedrockSubChunk subChunk) throws IOException;

}
