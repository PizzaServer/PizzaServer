package io.github.willqi.pizzaserver.server.world.providers;

import io.github.willqi.pizzaserver.format.api.LevelData;
import io.github.willqi.pizzaserver.server.world.providers.actions.ImplRequestChunkProcessingAction;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Handles world operations regarding loading/saving
 */
public abstract class BaseWorldProvider implements Closeable {

    private final File worldFile;


    public BaseWorldProvider(File worldFile) throws IOException {
        this.worldFile = worldFile;
    }

    /**
     * Retrieve information about the level
     * @return {@link LevelData}
     */
    public abstract LevelData getLevelData();

    /**
     * Retrieve a chunk from the provider
     * @param action Contains the completablefuture to resolve and the x and z coordinates
     */
    public abstract void onChunkRequest(ImplRequestChunkProcessingAction action);

    public File getWorldFile() {
        return this.worldFile;
    }

}
