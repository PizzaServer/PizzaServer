package io.github.pizzaserver.api.level;

import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;

import java.util.concurrent.CompletableFuture;

public interface LevelManager {

    /**
     * Check if a {@link Level} is currently loaded and being ticked.
     * @param name name of the level in the levels directory
     * @return name of the level
     */
    boolean isLevelLoaded(String name);

    /**
     * Retrieve the {@link Level} associated with the server configuration that all players spawn at by default.
     * @return default level
     */
    Level getDefaultLevel();

    /**
     * Get a {@link Level} by its name.
     * If the level is not currently loaded, it will fetch the level from the file system.
     * @param name name of the level
     * @return {@link Level} if any exists or null
     */
    Level getLevel(String name);

    CompletableFuture<Level> getLevelAsync(String name);

    /**
     * Get a specific {@link Dimension} belonging to a {@link Level}.
     * If the level is not currently loaded, it will fetch the level from the file system
     * @param levelName name of the level
     * @param dimension dimension type
     * @return {@link World} representative of that dimension or null if the dimension/world does not exist
     */
    World getLevelDimension(String levelName, Dimension dimension);

    /**
     * Unload a {@link Level} back to the file system.
     * @param name name of the level
     */
    void unloadLevel(String name);

    /**
     * Unload a {@link Level} back to the file system.
     * @param name name of the level
     */
    CompletableFuture<Void> unloadLevelAsync(String name);

}
