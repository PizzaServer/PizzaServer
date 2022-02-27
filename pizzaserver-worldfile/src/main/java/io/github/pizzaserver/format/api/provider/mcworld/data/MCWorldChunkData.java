package io.github.pizzaserver.format.api.provider.mcworld.data;

import io.github.pizzaserver.format.api.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.api.dimension.chunks.BedrockHeightMap;

public class MCWorldChunkData {

    private final BedrockHeightMap heightMap;
    private final BedrockBiomeMap biomeMap;


    public MCWorldChunkData(BedrockHeightMap heightMap, BedrockBiomeMap biomeMap) {
        this.heightMap = heightMap;
        this.biomeMap = biomeMap;
    }

    public BedrockHeightMap getHeightMap() {
        return this.heightMap;
    }

    public BedrockBiomeMap getBiomeMap() {
        return this.biomeMap;
    }

}
