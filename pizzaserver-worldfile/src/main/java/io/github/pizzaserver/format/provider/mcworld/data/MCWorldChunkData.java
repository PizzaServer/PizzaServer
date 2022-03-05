package io.github.pizzaserver.format.provider.mcworld.data;

import io.github.pizzaserver.format.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.BedrockHeightMap;

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
