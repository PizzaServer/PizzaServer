package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.entity.APIEntity;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.api.world.blocks.APIBlock;
import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.blocks.Block;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.providers.WorldProvider;
import io.github.willqi.pizzaserver.server.world.providers.WorldProviderThread;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class World implements Closeable, APIWorld {

    private final APIServer server;

    private final WorldProviderThread worldThread;

    private final APIChunkManager chunkManager = new ChunkManager(this);

    private final Set<APIPlayer> players = new HashSet<>();


    public World(APIServer server, WorldProvider provider) throws IOException {
        this.server = server;
        this.worldThread = new WorldProviderThread(this, provider);
        this.worldThread.start();
    }

    @Override
    public String getName() {
        return this.worldThread.getProvider().getName();
    }

    @Override
    public APIServer getServer() {
        return this.server;
    }

    @Override
    public Set<APIPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public APIChunkManager getChunkManager() {
        return this.chunkManager;
    }

    public WorldProviderThread getWorldThread() {
        return this.worldThread;
    }

    @Override
    public APIBlock getBlock(Vector3i position) {
        return this.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public APIBlock getBlock(int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot get block in unloaded chunk");
        }
        return this.getChunkManager().getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
    }

    @Override
    public void setBlock(APIBlockType blockType, Vector3i position) {
        this.setBlock(new Block(blockType), position);
    }

    @Override
    public void setBlock(APIBlockType blockType, int x, int y, int z) {
        this.setBlock(new Block(blockType), x, y, z);
    }

    @Override
    public void setBlock(APIBlock block, Vector3i position) {
        this.setBlock(block, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(APIBlock block, int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot set block in unloaded chunk");
        }
        this.getChunkManager().getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    @Override
    public void addEntity(APIEntity entity, Vector3 position) {
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
        ((Entity)entity).onSpawned();
        ((Entity)entity).setSpawned(true);
    }

    @Override
    public void removeEntity(APIEntity entity) {
        if (!entity.hasSpawned()) {
            throw new IllegalStateException("This entity has not been spawned");
        }
        entity.getLocation().getChunk().removeEntity(entity);
        if (entity instanceof Player) {
            this.players.remove(entity);
        }
    }

    @Override
    public void close() throws IOException {
        this.worldThread.close();
    }

    @Override
    public int hashCode() {
        return 43 + (43 * this.getName().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof World) {
            World otherWorld = (World)obj;
            return otherWorld.getName().equals(this.getName());
        }
        return false;
    }
}
