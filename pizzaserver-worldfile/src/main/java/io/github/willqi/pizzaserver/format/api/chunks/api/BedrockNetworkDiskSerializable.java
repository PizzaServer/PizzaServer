package io.github.willqi.pizzaserver.format.api.chunks.api;

import io.github.willqi.pizzaserver.format.mcworld.BlockRuntimeMapper;

import java.io.IOException;

public interface BedrockNetworkDiskSerializable {

    byte[] serializeForDisk() throws IOException;

    byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) throws IOException;

}
