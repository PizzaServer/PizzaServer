package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.blocks.Block;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.providers.WorldProvider;
import io.github.willqi.pizzaserver.server.world.providers.WorldProviderThread;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class World implements Closeable {

    private final Server server;

    private final WorldProviderThread worldThread;

    private final ChunkManager chunkManager = new ChunkManager(this);

    private final Set<Player> players = new HashSet<>();


    public World(Server server, WorldProvider provider) throws IOException {
        this.server = server;
        this.worldThread = new WorldProviderThread(this, provider);
        this.worldThread.start();
    }

    public Block getBlock(Vector3i position) {
        return this.getBlock(position.getX(), position.getY(), position.getZ());
    }

    public Block getBlock(int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot get block in unloaded chunk");
        }
        return this.getChunkManager().getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
    }

    public void setBlock(BlockType blockType, Vector3i position) {
        this.setBlock(new Block(blockType), position);
    }

    public void setBlock(BlockType blockType, int x, int y, int z) {
        this.setBlock(new Block(blockType), x, y, z);
    }

    public void setBlock(Block block, Vector3i position) {
        this.setBlock(block, position.getX(), position.getY(), position.getZ());
    }

    public void setBlock(Block block, int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot set block in unloaded chunk");
        }
        this.getChunkManager().getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    /**
     * Add a {@link Entity} to this world and spawn it to {@link Player}s
     * @param entity The {@link Entity} to spawn
     * @param position The position to spawn it in this world
     */
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

    /**
     * Despawn a {@link Entity} from this world and all of the {@link Player}s who could see it
     * @param entity the {@link Entity} to despawn
     */
    public void removeEntity(Entity entity) {
        if (!entity.hasSpawned()) {
            throw new IllegalStateException("This entity has not been spawned");
        }
        entity.getLocation().getChunk().removeEntity(entity);
        if (entity instanceof Player) {
            this.players.remove(entity);
        }
    }

    public String getName() {
        return this.worldThread.getProvider().getName();
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

    public WorldProviderThread getWorldThread() {
        return this.worldThread;
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
