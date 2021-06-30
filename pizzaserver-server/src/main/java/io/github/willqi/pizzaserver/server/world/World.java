package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.mcworld.MCWorld;
import io.github.willqi.pizzaserver.mcworld.world.info.MCWorldInfo;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class World implements Closeable {

    private final Server server;

    private final File worldDirectory;
    private final MCWorld mcWorld;
    private final MCWorldInfo worldInfo;

    private final ChunkManager chunkManager = new ChunkManager(this);


    public World(Server server, File worldDirectory) throws IOException {
        this.worldDirectory = worldDirectory;
        this.mcWorld = new MCWorld(worldDirectory);
        this.worldInfo = this.mcWorld.getWorldInfo();
        this.server = server;

        this.chunkManager.start();  // Start the ChunkThread
    }

    public String getWorldFileName() {
        return this.worldDirectory.getName();
    }

    public String getName() {
        return this.worldInfo.getWorldName();
    }

    public Server getServer() {
        return this.server;
    }

    public ChunkManager getChunkManager() {
        return this.chunkManager;
    }

    public MCWorld getInternalMCWorld() {
        return this.mcWorld;
    }

    @Override
    public void close() {
        this.chunkManager.stop();
    }

}
