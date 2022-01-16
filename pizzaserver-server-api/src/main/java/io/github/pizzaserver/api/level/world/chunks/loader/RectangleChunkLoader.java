package io.github.pizzaserver.api.level.world.chunks.loader;


import com.nukkitx.math.vector.Vector2i;

import java.util.HashSet;
import java.util.Set;

/**
 * Rectangular {@link ChunkLoader}.
 */
public class RectangleChunkLoader implements ChunkLoader {

    protected final Set<Vector2i> coordinates = new HashSet<>();


    public RectangleChunkLoader(Vector2i cornerA, Vector2i cornerB) {
        this(cornerA.getX(), cornerA.getY(), cornerB.getX(), cornerB.getY());
    }

    public RectangleChunkLoader(int x, int z, int x2, int z2) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);

        for (int chunkX = minX; chunkX <= maxX; chunkX++) {
            for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                this.coordinates.add(Vector2i.from(chunkX, chunkZ));
            }
        }
    }

    @Override
    public Set<Vector2i> getCoordinates() {
        return this.coordinates;
    }
}
