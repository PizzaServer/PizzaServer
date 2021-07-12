package io.github.willqi.pizzaserver.format.api.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.api.BedrockNetworkDiskSerializable;

import java.util.List;

public interface BedrockSubChunk extends BedrockNetworkDiskSerializable {

    List<BlockLayer> getLayers();

    BlockLayer getLayer(int index);

    void addLayer(BlockLayer layer);

}
