package io.github.pizzaserver.format.dimension.chunks;

import com.nukkitx.math.vector.Vector2i;
import io.github.pizzaserver.commons.utils.Check;

public class BedrockHeightMap {

    private final int[] heightMap = new int[256];

    /**
     * Retrieve the tallest block at a specific coordinate.
     * @param position The {@link Vector2i} that is representative of the coordinates in the chunk we need the height of.
     * @return height
     */
    public int getHighestBlockAt(Vector2i position) {
        return this.getHighestBlockAt(position.getX(), position.getY());
    }

    /**
     * Retrieve the tallest block at a specific coordinate.
     * @param x x coordinate
     * @param z z coordinate
     * @return height
     */
    public int getHighestBlockAt(int x, int z) {
        Check.withinBoundsInclusive(x, 0, 15, "x");
        Check.withinBoundsInclusive(z, 0, 15, "z");

        return this.heightMap[getChunkPosIndex(x, z)];
    }

    /**
     * Change the height map at a specific block in this chunk.
     * @param position coordinates
     * @param newHeight new height
     */
    public void setHighestBlockAt(Vector2i position, int newHeight) {
        this.setHighestBlockAt(position.getX(), position.getY(), newHeight);
    }

    /**
     * Change the height map at a specific block in this chunk.
     * @param x x coordinate
     * @param z z coordinate
     * @param newHeight new height
     */
    public void setHighestBlockAt(int x, int z, int newHeight) {
        Check.withinBoundsInclusive(x, 0, 15, "x");
        Check.withinBoundsInclusive(z, 0, 15, "z");

        this.heightMap[getChunkPosIndex(x, z)] = newHeight;
    }

    public int[] array() {
        return this.heightMap;
    }

    private static int getChunkPosIndex(int x, int z) {
        return z << 4 | x;
    }

}
