package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.server.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class WorldManager {

    private final Server server;

    // fileName : World
    private final Map<String, World> worlds = new ConcurrentHashMap<>();


    public WorldManager(Server server) {
        this.server = server;
    }

    public World getWorld(String name) {
        return this.worlds.getOrDefault(name, null);
    }

    public CompletableFuture<World> loadWorld(String pathToWorldDirectory) {
        return this.loadWorld(new File(pathToWorldDirectory));
    }

    public CompletableFuture<World> loadWorld(File worldDirectory) {
        return CompletableFuture.supplyAsync(() -> {
            World world;
            try {
                if (!worldDirectory.exists()) {
                    throw new FileNotFoundException("The file provided does not exist.");
                }
                if (worldDirectory.isFile()) {
                    throw new FileNotFoundException("The file provided is not a directory.");
                }

                world = new World(this.server, worldDirectory);
            } catch (IOException exception) {
                throw new CompletionException(exception);
            }
            this.worlds.put(worldDirectory.getName(), world);
            return world;
        });
    }

    public CompletableFuture<Void> unloadWorld(String name) {
        return CompletableFuture.supplyAsync(() -> {
            World world = this.worlds.get(name);
            if (world != null) {
                world.close();
                this.worlds.remove(name);
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
