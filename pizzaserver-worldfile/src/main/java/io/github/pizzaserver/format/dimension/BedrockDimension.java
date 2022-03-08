package io.github.pizzaserver.format.dimension;

import io.github.pizzaserver.format.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.provider.BedrockProvider;

import java.io.IOException;

public class BedrockDimension {

    private final int dimensionId;
    private final BedrockProvider provider;


    public BedrockDimension(BedrockProvider provider, int dimensionId) {
        this.provider = provider;
        this.dimensionId = dimensionId;
    }

    public BedrockChunk getChunk(int x, int z) throws IOException {
        return this.provider.getChunk(this.dimensionId, x, z);
    }

    public void saveChunk(BedrockChunk chunk) throws IOException {
        this.provider.saveChunk(chunk);
    }

}
