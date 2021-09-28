package io.github.willqi.pizzaserver.server.level;

import io.github.willqi.pizzaserver.api.level.LevelManager;
import io.github.willqi.pizzaserver.commons.utils.ReadWriteKeyLock;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.level.providers.BaseLevelProvider;
import io.github.willqi.pizzaserver.server.level.providers.ProviderType;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImplLevelManager implements LevelManager, Closeable {

    private final ImplServer server;

    // fileName : Level
    private final Map<String, ImplLevel> levels = new ConcurrentHashMap<>();
    private final ReadWriteKeyLock<String> locks = new ReadWriteKeyLock<>();


    public ImplLevelManager(ImplServer server) {
        this.server = server;
    }

    public ImplServer getServer() {
        return this.server;
    }

    /**
     * Ticks all the levels loaded.
     */
    public void tick() {
        for (ImplLevel level : this.levels.values()) {
            level.tick();
        }
    }

    @Override
    public boolean isLevelLoaded(String name) {
        return this.levels.containsKey(name);
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
    public ImplWorld getLevelDimension(String levelName, Dimension dimension) {
        ImplLevel level = this.getLevel(levelName);
        if (level == null) {
            return null;
        }
        return level.getDimension(dimension);
    }

    private ImplLevel fetchLevel(String name) {
        this.server.getLogger().info("Loading world: " + name);
        File file = Paths.get(this.server.getRootDirectory(), "levels", name).toFile();
        if (!file.exists()) {
            this.server.getLogger().error("No level exists with the name: " + name);
            return null;
        }
        BaseLevelProvider provider;
        try {
            provider = ProviderType.resolveByFile(file).create(file);
        } catch (IOException exception) {
            this.server.getLogger().error("Failed to create world provider with level: " + name, exception);
            return null;
        }
        this.server.getLogger().info("Successfully loaded level " + name);
        return new ImplLevel(this.server, provider);
    }

    @Override
    public void unloadLevel(String name) {
        this.locks.writeLock(name);

        this.server.getLogger().info("Unloading level " + name);
        try {
            ImplLevel level = this.levels.getOrDefault(name, null);
            if (level == null) {
                this.server.getLogger().error("No level is loaded with the name: " + name);
                return;
            }
            try {
                level.save();
                level.close();
            } catch (IOException exception) {
                this.server.getLogger().error("Failed to unload level: " + name, exception);
                return;
            }

            this.server.getLogger().info("Successfully unloaded level " + name);
            this.levels.remove(name);
        } finally {
            this.locks.writeUnlock(name);
        }
    }

    @Override
    public void unloadLevel(String name, boolean async) {
        if (async) {
            this.getServer().getScheduler()
                    .prepareTask(() -> this.unloadLevel(name))
                    .setAsynchronous(true)
                    .schedule();
        } else {
            this.unloadLevel(name);
        }
    }

    @Override
    public void close() throws IOException {
        for (ImplLevel level : this.levels.values()) {
            this.unloadLevel(level.getProvider().getFileName());
        }
    }
}
