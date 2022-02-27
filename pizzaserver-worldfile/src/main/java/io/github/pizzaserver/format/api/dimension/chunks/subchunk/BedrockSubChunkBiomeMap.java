package io.github.pizzaserver.format.api.dimension.chunks.subchunk;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.format.api.dimension.chunks.subchunk.utils.Palette;

public class BedrockSubChunkBiomeMap implements Cloneable {

    private Palette<Integer> palette;
    private int[] biomes = new int[4096];


    public BedrockSubChunkBiomeMap(Palette<Integer> palette) {
        this.palette = palette;
    }

    public Palette<Integer> getPalette() {
        return this.palette;
    }

    /**
     * Retrieve the biome data at a specific coordinate.
     * @param position The coordinates in the chunk we need the biome of
     * @return the biome
     */
    public int getBiomeAt(Vector3i position) {
        return this.getBiomeAt(position.getX(), position.getY(), position.getZ());
    }

    /**
     * Retrieve the biome data at a specific coordinate.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return the biome
     */
    public int getBiomeAt(int x, int y, int z) {
        return this.biomes[getChunkPosIndex(x, y, z)];
    }

    /**
     * Set the biome data at a specific position.
     * @param position The coordinates in the chunk we are changing the biome of
     * @param biome the new biome
     */
    public void setBiomeAt(Vector3i position, int biome) {
        this.setBiomeAt(position.getX(), position.getY(), position.getZ(), biome);
    }

    /**
     * Set the biome data at a specific location.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param biome the new biome we are changing this block column to
     */
    public void setBiomeAt(int x, int y, int z, int biome) {
        this.biomes[getChunkPosIndex(x, y, z)] = biome;
    }

    @Override
    public BedrockSubChunkBiomeMap clone() {
        try {
            BedrockSubChunkBiomeMap subChunkBiomeMap = (BedrockSubChunkBiomeMap) super.clone();
            subChunkBiomeMap.palette = this.palette.clone();

            int[] copiedBiomes = new int[4096];
            System.arraycopy(this.biomes, 0, copiedBiomes, 0, copiedBiomes.length);
            subChunkBiomeMap.biomes = copiedBiomes;

            return subChunkBiomeMap;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Clone threw exception");
        }
    }

    private static int getChunkPosIndex(int x, int y, int z) {
        return x << 8 | x << 4 | y;
    }

}
