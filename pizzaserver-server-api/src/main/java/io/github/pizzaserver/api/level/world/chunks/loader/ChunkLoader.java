package io.github.pizzaserver.api.level.world.chunks.loader;

import com.nukkitx.math.vector.Vector2i;

import java.util.Set;

/**
 * Keeps chunks loaded in a specific region.
 */
public interface ChunkLoader {

    /**
     * Retrieve the coordinates of every chunk loaded by this chunk loader.
     *
     * @return coordinates
     */
    Set<Vector2i> getCoordinates();
}
