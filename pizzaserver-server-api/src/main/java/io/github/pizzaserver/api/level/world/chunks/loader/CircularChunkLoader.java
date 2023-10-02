package io.github.pizzaserver.api.level.world.chunks.loader;


import org.cloudburstmc.math.vector.Vector2i;

import java.util.HashSet;
import java.util.Set;

/**
 * Circular {@link ChunkLoader}.
 */
public class CircularChunkLoader implements ChunkLoader {

    protected final Set<Vector2i> coordinates = new HashSet<>();


    public CircularChunkLoader(Vector2i coordinates, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                if (radius > distance) {
                    this.coordinates.add(Vector2i.from(coordinates.getX() + x, coordinates.getY() + z));
                }
            }
        }
    }

    @Override
    public Set<Vector2i> getCoordinates() {
        return this.coordinates;
    }

}
