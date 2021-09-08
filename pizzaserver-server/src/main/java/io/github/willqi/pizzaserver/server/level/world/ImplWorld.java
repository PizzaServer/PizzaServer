package io.github.willqi.pizzaserver.server.level.world;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.server.level.ImplLevel;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunkManager;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ImplWorld implements Closeable, World {

    private final ImplLevel level;
    private final Dimension dimension;
    private final ImplChunkManager chunkManager = new ImplChunkManager(this);

    private Vector3i spawnCoordinates;

    private final Set<Player> players = new HashSet<>();


    public ImplWorld(ImplLevel level, Dimension dimension) {
        this.level = level;
        this.dimension = dimension;

        Vector3i spawnCoordinates = this.level.getProvider().getLevelData().getWorldSpawn();
        if (spawnCoordinates.getY() > 255) {    // Hack to get around Minecraft worlds having REALLY high y spawn coordinates in the level.dat
            spawnCoordinates = new Vector3i(spawnCoordinates.getX(),
                    this.getHighestBlockAt(spawnCoordinates.getX(), spawnCoordinates.getZ()),
                    spawnCoordinates.getZ());
        }
        this.setSpawnCoordinates(spawnCoordinates);
    }

    @Override
    public ImplServer getServer() {
        return this.getLevel().getServer();
    }

    @Override
    public ImplLevel getLevel() {
        return this.level;
    }

    @Override
    public Dimension getDimension() {
        return this.dimension;
    }

    @Override
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public ImplChunkManager getChunkManager() {
        return this.chunkManager;
    }

    @Override
    public Vector3i getSpawnCoordinates() {
        return this.spawnCoordinates;
    }

    @Override
    public void setSpawnCoordinates(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
    }

    @Override
    public Chunk getChunk(int x, int z) {
        return this.getChunkManager().getChunk(x, z);
    }

    @Override
    public int getHighestBlockAt(Vector2i coordinates) {
        return this.getHighestBlockAt(coordinates.getX(), coordinates.getY());
    }

    @Override
    public int getHighestBlockAt(int x, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        return this.getChunk(chunkX, chunkZ).getHighestBlockAt(x % 16, z % 16);
    }

    @Override
    public Block getBlock(Vector3i position) {
        return this.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        return this.getChunkManager().getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
    }

    @Override
    public void setBlock(String blockId, Vector3i position) {
        this.setBlock(blockId, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(String blockId, int x, int y, int z) {
        BaseBlockType blockType = BlockRegistry.getBlockType(blockId);
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
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunkManager().getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    public void sendBlock(Player player, Vector3i blockCoordinates) {
        this.sendBlock(player, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    public void sendBlock(Player player, int x, int y, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunkManager().getChunk(chunkX, chunkZ).sendBlock(player, x % 16, y, z % 16);
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
        Location location = new Location(this, position);

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
        ImplChunk chunk = baseEntity.getChunk();
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

    /**
     * Retrieve the default save data for a player spawning in this world
     * @return default {@link PlayerData} for players spawning in this world
     */
    public PlayerData getDefaultPlayerData() {
        return new PlayerData.Builder()
                .setLevelName(this.getLevel().getProvider().getFileName())
                .setDimension(this.getDimension())
                .setPosition(this.getSpawnCoordinates().add(0, 2, 0).toVector3())
                .setYaw(this.getServer().getConfig().getDefaultYaw())
                .setPitch(this.getServer().getConfig().getDefaultPitch())
                .build();
    }

    @Override
    public void close() throws IOException {
        this.getChunkManager().close();
    }

    @Override
    public int hashCode() {
        return 43 + (43 * this.getDimension().hashCode()) + (43 * this.getLevel().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplWorld) {
            ImplWorld otherWorld = (ImplWorld)obj;
            return otherWorld.getDimension().equals(this.getDimension()) && otherWorld.getLevel().equals(this.getLevel());
        }
        return false;
    }

    private static int getChunkCoordinate(int i) {
        return (int)Math.floor(i / 16d);
    }

}
