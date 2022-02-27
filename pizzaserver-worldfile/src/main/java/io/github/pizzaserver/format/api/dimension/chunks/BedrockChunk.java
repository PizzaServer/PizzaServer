package io.github.pizzaserver.format.api.dimension.chunks;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.api.provider.BedrockChunkProvider;

import java.io.IOException;
import java.util.*;

/**
 * Represents a 16x16 chunk of blocks in a Minecraft world.
 */
public class BedrockChunk {

    private final int dimension;
    private final int x;
    private final int z;

    private byte version;

    private BedrockBiomeMap biomeMap = new BedrockBiomeMap();
    private BedrockHeightMap heightMap = new BedrockHeightMap();

    private Set<NbtMap> entities = new HashSet<>();
    private Set<NbtMap> blockEntities = new HashSet<>();

    private final Map<Integer, BedrockSubChunk> subChunks = new HashMap<>();
    private final BedrockChunkProvider chunkProvider;


    public BedrockChunk(BedrockChunkProvider chunkProvider, int dimension, int x, int z) {
        this.chunkProvider = chunkProvider;
        this.dimension = dimension;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public int getDimension() {
        return this.dimension;
    }

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public Set<NbtMap> getEntities() {
        return Collections.unmodifiableSet(this.entities);
    }

    public void setEntities(Set<NbtMap> entities) {
        this.entities = entities;
    }

    public boolean addEntity(NbtMap entityNBT) {
        return this.entities.add(entityNBT);
    }

    public boolean removeEntity(NbtMap entityNBT) {
        return this.entities.remove(entityNBT);
    }

    public Set<NbtMap> getBlockEntities() {
        return Collections.unmodifiableSet(this.blockEntities);
    }

    public void setBlockEntities(Set<NbtMap> blockEntities) {
        this.blockEntities = blockEntities;
    }

    public boolean addBlockEntity(NbtMap blockEntityNBT) {
        return this.blockEntities.add(blockEntityNBT);
    }

    public boolean removeBlockEntity(NbtMap blockEntityNBT) {
        return this.blockEntities.remove(blockEntityNBT);
    }

    /**
     * A height map is a array of 256 (16 * 16) integers that stores the highest blocks in a chunk.
     * @return int array
     */
    public BedrockHeightMap getHeightMap() {
        return this.heightMap;
    }

    public void setHeightMap(BedrockHeightMap heightMap) {
        this.heightMap = heightMap;
    }

    public BedrockBiomeMap getBiomeMap() {
        return this.biomeMap;
    }

    public void setBiomeMap(BedrockBiomeMap biomeMap) {
        this.biomeMap = biomeMap;
    }

    /**
     * Retrieve a specific {@link BedrockSubChunk} in this chunk.
     * @param index the index of the subchunk
     * @return the subchunk requested
     */
    public BedrockSubChunk getSubChunk(int index) throws IOException {
        if (this.subChunks.containsKey(index)) {
            return this.subChunks.get(index);
        } else {
            BedrockSubChunk subChunk = this.chunkProvider.getSubChunk(this.getDimension(), this.getX(), this.getZ(), index);
            this.subChunks.putIfAbsent(index, subChunk);

            return subChunk;
        }
    }

    public Map<Integer, BedrockSubChunk> getLoadedSubChunks() {
        return Collections.unmodifiableMap(this.subChunks);
    }

}
