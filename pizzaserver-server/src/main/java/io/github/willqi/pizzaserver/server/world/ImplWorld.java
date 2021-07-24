package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.blocks.Block;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.github.willqi.pizzaserver.server.world.blocks.ImplBlock;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunkManager;
import io.github.willqi.pizzaserver.server.world.providers.BaseWorldProvider;
import io.github.willqi.pizzaserver.server.world.providers.WorldProviderThread;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ImplWorld implements Closeable, World {

    private final Server server;

    private final WorldProviderThread worldThread;

    private final ChunkManager chunkManager = new ImplChunkManager(this);

    private final Set<Player> players = new HashSet<>();


    public ImplWorld(Server server, BaseWorldProvider provider) throws IOException {
        this.server = server;
        this.worldThread = new WorldProviderThread(this, provider);
        this.worldThread.start();
    }

    @Override
    public String getName() {
        return this.worldThread.getProvider().getName();
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.chunkManager;
    }

    public WorldProviderThread getWorldThread() {
        return this.worldThread;
    }

    @Override
    public Block getBlock(Vector3i position) {
        return this.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot get block in unloaded chunk");
        }
        return this.getChunkManager().getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
    }

    @Override
    public void setBlock(BlockType blockType, Vector3i position) {
        this.setBlock(new ImplBlock(blockType), position);
    }

    @Override
    public void setBlock(BlockType blockType, int x, int y, int z) {
        this.setBlock(new ImplBlock(blockType), x, y, z);
    }

    @Override
    public void setBlock(Block block, Vector3i position) {
        this.setBlock(block, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(Block block, int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (!this.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            throw new NullPointerException("Cannot set block in unloaded chunk");
        }
        this.getChunkManager().getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    @Override
    public void addEntity(Entity entity, Vector3 position) {
        if (entity.hasSpawned()) {
            throw new IllegalStateException("This entity has already been spawned");
        }

        ImplLocation location = new ImplLocation(this, position);
        if (location.getChunk() == null) {
            throw new NullPointerException("This entity cannot be spawned in an unloaded chunk");
        }

        entity.setLocation(location);
        if (entity instanceof ImplPlayer) {
            this.players.add((ImplPlayer)entity);
        }
        ((BaseEntity)entity).onSpawned();
        ((BaseEntity)entity).setSpawned(true);
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!entity.hasSpawned()) {
            throw new IllegalStateException("This entity has not been spawned");
        }
        entity.getLocation().getChunk().removeEntity(entity);
        if (entity instanceof ImplPlayer) {
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
        if (obj instanceof ImplWorld) {
            ImplWorld otherWorld = (ImplWorld)obj;
            return otherWorld.getName().equals(this.getName());
        }
        return false;
    }
}
