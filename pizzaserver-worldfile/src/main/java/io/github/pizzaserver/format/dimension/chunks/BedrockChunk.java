package io.github.pizzaserver.format.dimension.chunks;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.provider.BedrockProvider;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<Integer, BedrockSubChunk> subChunks = new ConcurrentHashMap<>();
    private final BedrockProvider chunkProvider;


    public BedrockChunk(BedrockProvider chunkProvider, int dimension, int x, int z) {
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

    public synchronized byte getVersion() {
        return this.version;
    }

    public synchronized void setVersion(byte version) {
        this.version = version;
    }

    public synchronized Set<NbtMap> getEntities() {
        return Collections.unmodifiableSet(this.entities);
    }

    public synchronized void setEntities(Set<NbtMap> entities) {
        this.entities = entities;
    }

    public synchronized boolean addEntity(NbtMap entityNBT) {
        return this.entities.add(entityNBT);
    }

    public synchronized boolean removeEntity(NbtMap entityNBT) {
        return this.entities.remove(entityNBT);
    }

    public synchronized Set<NbtMap> getBlockEntities() {
        return Collections.unmodifiableSet(this.blockEntities);
    }

    public synchronized void setBlockEntities(Set<NbtMap> blockEntities) {
        this.blockEntities = blockEntities;
    }

    public synchronized boolean addBlockEntity(NbtMap blockEntityNBT) {
        return this.blockEntities.add(blockEntityNBT);
    }

    public synchronized boolean removeBlockEntity(NbtMap blockEntityNBT) {
        return this.blockEntities.remove(blockEntityNBT);
    }

    /**
     * A height map is a array of 256 (16 * 16) integers that stores the highest blocks in a chunk.
     * @return int array
     */
    public synchronized BedrockHeightMap getHeightMap() {
        return this.heightMap;
    }

    public synchronized void setHeightMap(BedrockHeightMap heightMap) {
        this.heightMap = heightMap;
    }

    public synchronized BedrockBiomeMap getBiomeMap() {
        return this.biomeMap;
    }

    public synchronized void setBiomeMap(BedrockBiomeMap biomeMap) {
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

}
