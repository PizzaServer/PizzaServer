package io.github.willqi.pizzaserver.api.world;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface APIWorldManager {

    /**
     * Retrieve a {@link APIWorld} by its directory name
     * @param name directory name of the world
     * @return {@link APIWorld}
     */
    APIWorld getWorld(String name);

    /**
     * Load a {@link APIWorld} by its directory name
     * @param worldName directory name of the world
     * @return {@link CompletableFuture<APIWorld>}
     */
    CompletableFuture<APIWorld> loadWorld(String worldName);

    /**
     * Load a {@link APIWorld} by its directory file
     * @param worldDirectory directory file
     * @return {@link CompletableFuture<APIWorld>}
     */
    CompletableFuture<APIWorld> loadWorld(File worldDirectory);

    /**
     * Unload a {@link APIWorld} by its directory name
     * @param worldName directory name of the world
     * @return {@link CompletableFuture<Void>}
     */
    CompletableFuture<Void> unloadWorld(String worldName);

}
