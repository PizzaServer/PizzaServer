package io.github.pizzaserver.server.level;

import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.LevelManager;
import io.github.pizzaserver.api.level.providers.ProviderType;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.commons.utils.ReadWriteKeyLock;
import io.github.pizzaserver.format.api.BedrockLevel;
import io.github.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.pizzaserver.format.api.chunks.BedrockChunkProvider;
import io.github.pizzaserver.format.mcworld.MCWorldLevel;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.level.processing.LevelChunkProcessorManager;
import io.github.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ImplLevelManager implements LevelManager, Closeable {

    private final ImplServer server;
    private final LevelChunkProcessorManager levelChunkProcessorManager;

    // fileName : Level
    private final Map<String, ImplLevel> levels = new ConcurrentHashMap<>();
    private final ReadWriteKeyLock<String> locks = new ReadWriteKeyLock<>();


    public ImplLevelManager(ImplServer server) {
        this.server = server;
        this.levelChunkProcessorManager = new LevelChunkProcessorManager(this);
    }

    public ImplServer getServer() {
        return this.server;
    }

    public LevelChunkProcessorManager getProcessorManager() {
        return this.levelChunkProcessorManager;
    }

    /**
     * Ticks all the levels loaded.
     */
    public void tick() {
        for (ImplLevel level : this.levels.values()) {
            level.tick();
        }
        this.levelChunkProcessorManager.tick();
    }

    @Override
    public boolean isLevelLoaded(String name) {
        return this.levels.containsKey(name);
    }

    @Override
    public ImplLevel getDefaultLevel() {
        return this.getLevel(this.getServer().getConfig().getDefaultWorldName());
    }

    @Override
    public ImplLevel getLevel(String name) {
        this.locks.readLock(name);
        this.levels.computeIfAbsent(name, ignored -> {
            this.locks.readUnlock(name);
            this.locks.writeLock(name); // Prevent multiple Threads from reading the directory at the same time

            ImplLevel level = null;
            try {
                if (!this.levels.containsKey(name)) {
                    level = this.fetchLevel(name);
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to fetch level by the name " + name, exception);
            } finally {
                this.locks.writeUnlock(name);
                this.locks.readLock(name);
            }
            return level;
        });
        try {
            return this.levels.getOrDefault(name, null);
        } finally {
            this.locks.readUnlock(name);
        }
    }

    @Override
    public CompletableFuture<Level> getLevelAsync(String name) {
        return CompletableFuture.supplyAsync(() -> this.getLevel(name));
    }

    @Override
    public ImplWorld getLevelDimension(String levelName, Dimension dimension) {
        ImplLevel level = this.getLevel(levelName);
        if (level == null) {
            return null;
        }
        return level.getDimension(dimension);
    }

    protected ImplLevel fetchLevel(String name) throws IOException {
        File file = this.getLevelFile(name);
        if (!file.exists()) {
            throw new FileNotFoundException("No level exists with the name: " + name);
        }
        BedrockLevel<? extends BedrockChunkProvider<? extends BedrockChunk>> provider;
        try {
            provider = this.getProvider(file, ProviderType.resolveByFile(file));
        } catch (IOException exception) {
            this.server.getLogger().error("Failed to create world provider with level: " + name, exception);
            return null;
        }
        this.server.getLogger().info("Successfully loaded level " + name);
        return new ImplLevel(this, provider);
    }

    @Override
    public void unloadLevel(String name) throws IOException {
        this.locks.writeLock(name);

        try {
            ImplLevel level = this.levels.getOrDefault(name, null);
            if (level != null) {
                level.save();
                level.close();
                this.levels.remove(name);
            }
        } finally {
            this.locks.writeUnlock(name);
        }
    }

    @Override
    public CompletableFuture<Void> unloadLevelAsync(String name) {
        CompletableFuture<Void> levelFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                this.unloadLevel(name);
                levelFuture.complete(null);
            } catch (IOException exception) {
                levelFuture.completeExceptionally(exception);
            }
        });

        return levelFuture;
    }

    @Override
    public Level createLevel(String name, ProviderType providerType) throws IOException {
        this.locks.writeLock(name);
        try {
            File file = this.getLevelFile(name);
            if (file.exists()) {
                throw new FileAlreadyExistsException(name + " is already a level that exists");
            }

            ImplLevel level = new ImplLevel(this, this.getProvider(file, providerType));
            this.levels.put(name, level);

            return level;
        } finally {
            this.locks.writeUnlock(name);
        }
    }

    @Override
    public CompletableFuture<Level> createLevelAsync(String name, ProviderType providerType) {
        CompletableFuture<Level> levelFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                levelFuture.complete(this.createLevel(name, providerType));
            } catch (IOException exception) {
                levelFuture.completeExceptionally(exception);
            }
        });

        return levelFuture;
    }

    protected File getLevelFile(String name) {
        return Paths.get(this.server.getRootDirectory(), "levels", name).toFile();
    }

    /**
     * Retrieve a provider object given a level file that may or may not exist.
     * If it does not exist, one will be created.
     *
     * @param levelFile    level file to read/write to
     * @param providerType provider type to open the file as
     * @return provider
     *
     * @throws IOException if an exception occurred while reading the file
     */
    protected BedrockLevel<? extends BedrockChunkProvider<? extends BedrockChunk>> getProvider(
            File levelFile, ProviderType providerType) throws IOException {
        switch (providerType) {
            case LEVELDB:
                return new MCWorldLevel(levelFile);
            default:
                return null;
        }
    }

    @Override
    public void close() throws IOException {
        for (ImplLevel level : this.levels.values()) {
            this.unloadLevel(level.getProvider().getFile().getName());
        }
        this.levelChunkProcessorManager.close();
    }
}
