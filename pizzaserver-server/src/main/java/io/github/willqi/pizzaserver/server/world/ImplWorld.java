package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.blocks.Block;
import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.api.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;
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
    public void setBlock(String blockId, Vector3i position) {
        this.setBlock(blockId, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(String blockId, int x, int y, int z) {
        BaseBlockType blockType = this.getServer().getBlockRegistry().getBlockType(blockId);
        this.setBlock(blockType, x, y, z);
    }

    @Override
    public void setBlock(BaseBlockType blockType, Vector3i position) {
        this.setBlock(new Block(blockType), position);
    }

    @Override
    public void setBlock(BaseBlockType blockType, int x, int y, int z) {
        this.setBlock(new Block(blockType), x, y, z);
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
        // Check if we need to despawn the entity from its old world first
        if (entity.getWorld() != null) {
            if (entity.getWorld().equals(this)) {
                throw new IllegalStateException("This entity already exists in this world.");
            } else {
                entity.getWorld().removeEntity(entity);
            }
        }
        Location location = new ImplLocation(this, position);

        if (entity instanceof Player) {
            this.players.add((Player)entity);
        }
        BaseEntity baseEntity = (BaseEntity)entity;
        baseEntity.setLocation(location);
        ((ImplChunk)location.getChunk()).addEntity(entity);
        baseEntity.onSpawned();
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!this.equals(entity.getWorld())) {
            throw new IllegalStateException("This entity has not been spawned in this world");
        }
        if (entity instanceof Player) {
            this.players.remove(entity);
        }
        BaseEntity baseEntity = (BaseEntity)entity;
        ImplChunk chunk = (ImplChunk)baseEntity.getChunk();
        baseEntity.setLocation(null);   // the entity no longer exists in any world
        baseEntity.onDespawned();
        chunk.removeEntity(baseEntity);
    }

    @Override
    public void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType, int blockID) {
        WorldSoundEventPacket packet = new WorldSoundEventPacket();
        packet.setSound(sound);
        packet.setVector3(vector3);
        packet.setGlobal(isGlobal);
        packet.setBaby(isBaby);
        packet.setEntityType(entityType);
        packet.setBlockID(blockID);
        WorldSoundEvent event = new WorldSoundEvent(this, sound, vector3, isGlobal, isBaby, entityType, blockID);
        getServer().getEventManager().call(event);
        if(!event.isCancelled()) {
            for(Player player : getPlayers()) {
                player.sendPacket(packet);
            }
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
