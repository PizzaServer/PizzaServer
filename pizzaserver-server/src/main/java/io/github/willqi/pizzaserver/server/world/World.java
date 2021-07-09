package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.mcworld.MCWorld;
import io.github.willqi.pizzaserver.mcworld.world.info.MCWorldInfo;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class World implements Closeable {

    private final Server server;

    private final File worldDirectory;
    private final MCWorld mcWorld;
    private final MCWorldInfo worldInfo;

    private final ChunkManager chunkManager = new ChunkManager(this);

    private final Set<Player> players = new HashSet<>();


    public World(Server server, File worldDirectory) throws IOException {
        this.worldDirectory = worldDirectory;
        this.mcWorld = new MCWorld(worldDirectory);
        this.worldInfo = this.mcWorld.getWorldInfo();
        this.server = server;

        this.chunkManager.start();  // Start the ChunkThread
    }

    public void addEntity(Entity entity, Vector3 position) {
        if (entity.hasSpawned()) {
            throw new IllegalStateException("This entity has already been spawned");
        }

        Location location = new Location(this, position);
        if (location.getChunk() == null) {
            throw new NullPointerException("This entity cannot be spawned in an unloaded chunk");
        }

        entity.setLocation(location);
        if (entity instanceof Player) {
            this.players.add((Player)entity);
        }
        entity.onSpawned();
        entity.setSpawned(true);
    }

    public void removeEntity(Entity entity) {
        if (!entity.hasSpawned()) {
            throw new IllegalStateException("This entity has not been spawned");
        }
        entity.getLocation().getChunk().removeEntity(entity);
        if (entity instanceof Player) {
            this.players.remove(entity);
        }
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

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(this.players);
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

    @Override
    public int hashCode() {
        return 43 + (43 * this.getWorldFileName().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof World) {
            World otherWorld = (World)obj;
            return otherWorld.getWorldFileName().equals(this.getWorldFileName());
        }
        return false;
    }
}
