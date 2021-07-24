package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.api.world.APIWorldManager;
import io.github.willqi.pizzaserver.server.world.providers.ProviderType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class WorldManager implements APIWorldManager {

    private final APIServer server;

    // fileName : World
    private final Map<String, APIWorld> worlds = new ConcurrentHashMap<>();


    public WorldManager(APIServer server) {
        this.server = server;
    }

    public APIServer getServer() {
        return this.server;
    }

    @Override
    public APIWorld getWorld(String name) {
        return this.worlds.getOrDefault(name, null);
    }

    @Override
    public CompletableFuture<APIWorld> loadWorld(String worldName) {
        return this.loadWorld(Paths.get(this.getServer().getRootDirectory(), "worlds", worldName).toFile());
    }

    @Override
    public CompletableFuture<APIWorld> loadWorld(File worldDirectory) {
        return CompletableFuture.supplyAsync(() -> {
            World world;
            try {
                if (!worldDirectory.exists()) {
                    throw new FileNotFoundException("The file provided does not exist.");
                }
                if (worldDirectory.isFile()) {
                    throw new FileNotFoundException("The file provided is not a directory.");
                }

                world = new World(this.server, ProviderType.resolveByFile(worldDirectory).create(worldDirectory));
            } catch (IOException exception) {
                throw new CompletionException(exception);
            }
            this.worlds.put(worldDirectory.getName(), world);
            return world;
        });
    }

    @Override
    public CompletableFuture<Void> unloadWorld(String name) {
        return CompletableFuture.supplyAsync(() -> {
            World world = (World)this.worlds.get(name);
            if (world != null) {
                try {
                    world.close();
                    this.worlds.remove(name);
                } catch (IOException exception) {
                    throw new CompletionException(exception);
                }
            }
            return null;
        });
    }

    public void loadWorlds() {
        File worldsDirectory = new File(this.server.getRootDirectory(), "worlds");
        for (File file : worldsDirectory.listFiles()) {
            this.server.getLogger().info("Loading world " + file.getName());
            try {
                this.loadWorld(file).get();
            } catch (ExecutionException exception) {
                this.server.getLogger().error("Failed to load world " + file.getName(), exception.getCause());
                continue;
            } catch (InterruptedException exception) {
                this.server.getLogger().error("Failed to load world " + file.getName(), exception);
                continue;
            }
            this.server.getLogger().info("Successfully loaded world " + file.getName());
        }
    }

    public void unloadWorlds() {
        Iterator<String> loadedWorldNamesIterator = this.worlds.keySet().iterator();
        while (loadedWorldNamesIterator.hasNext()) {
            String worldName = loadedWorldNamesIterator.next();

            this.server.getLogger().info("Unloading world " + worldName);
            try {
                this.unloadWorld(worldName).get();
            } catch (ExecutionException exception) {
                this.server.getLogger().error("Failed to unload world " + worldName, exception.getCause());
                continue;
            } catch (InterruptedException exception) {
                this.server.getLogger().error("Failed to unload world " + worldName, exception);
                continue;
            }
            this.server.getLogger().info("Successfully unloaded world " + worldName);
        }
    }

}
