package io.github.willqi.pizzaserver.api.level;

import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.commons.world.Dimension;

public interface LevelManager {

    /**
     * Check if a {@link Level} is currently loaded and being ticked
     * @param name name of the level in the levels directory
     * @return name of the level
     */
    boolean isLevelLoaded(String name);

    /**
     * Get a {@link Level} by its name.
     * If the level is not currently loaded, it will fetch the level from the file system.
     * @param name name of the level
     * @return {@link Level} if any exists or null
     */
    Level getLevel(String name);

    /**
     * Get a specific {@link Dimension} belonging to a {@link Level}
     * If the level is not currently loaded, it will fetch the level from the file system
     * @param levelName name of the level
     * @param dimension dimension type
     * @return {@link World} representative of that dimension or null if the dimension/world does not exist
     */
    World getLevelDimension(String levelName, Dimension dimension);

    /**
     * Unload a {@link Level} back to the file system
     * @param name name of the level
     * @return if the {@link Level} was unloaded
     */
    boolean unloadLevel(String name);

}
