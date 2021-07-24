package io.github.willqi.pizzaserver.api.world;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface WorldManager {

    /**
     * Retrieve a {@link World} by its directory name
     * @param name directory name of the world
     * @return {@link World}
     */
    World getWorld(String name);

    /**
     * Load a {@link World} by its directory name
     * @param worldName directory name of the world
     * @return {@link CompletableFuture< World >}
     */
    CompletableFuture<World> loadWorld(String worldName);

    /**
     * Load a {@link World} by its directory file
     * @param worldDirectory directory file
     * @return {@link CompletableFuture< World >}
     */
    CompletableFuture<World> loadWorld(File worldDirectory);

    /**
     * Unload a {@link World} by its directory name
     * @param worldName directory name of the world
     * @return {@link CompletableFuture<Void>}
     */
    CompletableFuture<Void> unloadWorld(String worldName);

}
