package io.github.pizzaserver.format.dimension.chunks;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.provider.BedrockProvider;

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

    private final Set<NbtMap> entities = new HashSet<>();
    private final Map<Vector3i, NbtMap> blockEntities = new HashMap<>();

    private final Map<Integer, BedrockSubChunk> subChunks = new HashMap<>();
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

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public Set<NbtMap> getEntities() {
        return Collections.unmodifiableSet(this.entities);
    }

    public boolean addEntity(NbtMap entityNBT) {
        return this.entities.add(entityNBT);
    }

    public boolean removeEntity(NbtMap entityNBT) {
        return this.entities.remove(entityNBT);
    }

    public Map<Vector3i, NbtMap> getBlockEntities() {
        return Collections.unmodifiableMap(this.blockEntities);
    }

    public Optional<NbtMap> getBlockEntity(int x, int y, int z) {
        return Optional.ofNullable(this.blockEntities.getOrDefault(Vector3i.from(x, y, z), null));
    }

    public void addBlockEntity(NbtMap blockEntityNBT) {
        int x = blockEntityNBT.getInt("x");
        int y = blockEntityNBT.getInt("y");
        int z = blockEntityNBT.getInt("z");

        this.addBlockEntity(x, y, z, blockEntityNBT);
    }

    public void addBlockEntity(int x, int y, int z, NbtMap blockEntityNBT) {
        this.removeBlockEntity(x, y, z);

        this.blockEntities.put(Vector3i.from(x, y, z), blockEntityNBT.toBuilder()
                .putInt("x", x)
                .putInt("y", y)
                .putInt("z", z)
                .build());
    }

    public boolean removeBlockEntity(int x, int y, int z) {
        return this.blockEntities.remove(Vector3i.from(x, y, z)) != null;
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

}
