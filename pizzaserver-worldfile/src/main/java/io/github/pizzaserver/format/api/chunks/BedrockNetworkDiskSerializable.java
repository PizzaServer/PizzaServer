package io.github.pizzaserver.format.api.chunks;

import io.github.pizzaserver.format.BlockRuntimeMapper;

import java.io.IOException;

public interface BedrockNetworkDiskSerializable {

    byte[] serializeForDisk() throws IOException;

    byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) throws IOException;

}
