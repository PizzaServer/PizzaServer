package io.github.pizzaserver.format.dimension.chunks;

import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunkBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.utils.Palette;

import java.util.*;

public class BedrockBiomeMap {

    private final Map<Integer, BedrockSubChunkBiomeMap> subChunkBiomes = new HashMap<>();


    /**
     * Retrieve a sub chunk biome map if one exists. Otherwise return null
     * @param subChunk sub chunk index
     * @return the biome map at that index if one exists
     */
    public synchronized BedrockSubChunkBiomeMap getSubChunk(int subChunk) {
        this.subChunkBiomes.computeIfAbsent(subChunk, ignored -> new BedrockSubChunkBiomeMap(new Palette<>()));
        return this.subChunkBiomes.get(subChunk);
    }

    /**
     * Set a sub chunk biome map at an index.
     * @param subChunk sub chunk index
     * @param biomeMap the biome map at that index
     */
    public synchronized void setSubChunk(int subChunk, BedrockSubChunkBiomeMap biomeMap) {
        this.subChunkBiomes.put(subChunk, biomeMap);
    }

    public synchronized List<BedrockSubChunkBiomeMap> getSubChunks() {
        return new ArrayList<>(this.subChunkBiomes.values());
    }

}
