package io.github.willqi.pizzaserver.mcworld.world.chunks.versions;

import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockSubChunk;
import io.netty.buffer.ByteBuf;

public abstract class SubChunkVersion {

    protected SubChunkVersion() {}

    public abstract BedrockSubChunk parse(ByteBuf buffer);

}
