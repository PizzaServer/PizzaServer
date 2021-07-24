package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.WorldManager;
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

public class BedrockWorldManager implements WorldManager {

    private final Server server;

    // fileName : World
    private final Map<String, World> worlds = new ConcurrentHashMap<>();


    public BedrockWorldManager(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public World getWorld(String name) {
        return this.worlds.getOrDefault(name, null);
    }

    @Override
    public CompletableFuture<World> loadWorld(String worldName) {
        return this.loadWorld(Paths.get(this.getServer().getRootDirectory(), "worlds", worldName).toFile());
    }

    @Override
    public CompletableFuture<World> loadWorld(File worldDirectory) {
        return CompletableFuture.supplyAsync(() -> {
            BedrockWorld world;
            try {
                if (!worldDirectory.exists()) {
                    throw new FileNotFoundException("The file provided does not exist.");
                }
                if (worldDirectory.isFile()) {
                    throw new FileNotFoundException("The file provided is not a directory.");
                }

                world = new BedrockWorld(this.server, ProviderType.resolveByFile(worldDirectory).create(worldDirectory));
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
            BedrockWorld world = (BedrockWorld)this.worlds.get(name);
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
