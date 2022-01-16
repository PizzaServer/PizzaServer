package io.github.pizzaserver.format.api.chunks;

import io.github.pizzaserver.format.MinecraftDataMapper;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface BedrockNetworkDiskSerializable {

    void serializeForDisk(ByteBuf buffer) throws IOException;

    void serializeForNetwork(ByteBuf buffer, MinecraftDataMapper runtimeMapper) throws IOException;
}
