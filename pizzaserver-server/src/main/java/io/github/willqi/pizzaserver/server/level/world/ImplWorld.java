package io.github.willqi.pizzaserver.server.level.world;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.chunks.loader.ChunkLoader;
import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.ImplEntity;
import io.github.willqi.pizzaserver.server.level.ImplLevel;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.api.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.level.world.chunks.WorldChunkManager;
import io.github.willqi.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.io.IOException;
import java.util.*;

public class ImplWorld implements World {

    private final ImplLevel level;
    private final Dimension dimension;
    private final WorldChunkManager chunkManager = new WorldChunkManager(this);

    private Vector3i spawnCoordinates;

    private final Set<Player> players = new HashSet<>();
    private final Map<Long, Entity> entities = new HashMap<>();


    public ImplWorld(ImplLevel level, Dimension dimension) {
        this.level = level;
        this.dimension = dimension;

        Vector3i worldSpawnCoordinates = this.level.getProvider().getLevelData().getWorldSpawn();
        if (worldSpawnCoordinates.getY() > 255) {    // Hack to get around Minecraft worlds having REALLY high y spawn coordinates in the level.dat
            worldSpawnCoordinates = new Vector3i(worldSpawnCoordinates.getX(),
                    this.getHighestBlockAt(worldSpawnCoordinates.getX(), worldSpawnCoordinates.getZ()),
                    worldSpawnCoordinates.getZ());
        }
        this.setSpawnCoordinates(worldSpawnCoordinates);
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
    public Vector3i getSpawnCoordinates() {
        return this.spawnCoordinates;
    }

    @Override
    public void setSpawnCoordinates(Vector3i coordinates) {
        this.spawnCoordinates = coordinates;
    }

    public WorldChunkManager getChunkManager() {
        return this.chunkManager;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.getChunkManager().isChunkLoaded(x, z);
    }

    @Override
    public ImplChunk getChunk(int x, int z) {
        return this.getChunkManager().getChunk(x, z);
    }

    @Override
    public ImplChunk getChunk(int x, int z, boolean loadFromProvider) {
        return this.getChunkManager().getChunk(x, z, loadFromProvider);
    }

    @Override
    public void sendChunk(Player player, int x, int z) {
        this.sendChunk(player, x, z, true);
    }

    @Override
    public void sendChunk(Player player, int x, int z, boolean async) {
        this.chunkManager.sendChunk(player, x, z, async);
    }

    @Override
    public boolean addChunkLoader(ChunkLoader chunkLoader) {
        return this.chunkManager.addChunkLoader(chunkLoader);
    }

    @Override
    public boolean removeChunkLoader(ChunkLoader chunkLoader) {
        return this.chunkManager.removeChunkLoader(chunkLoader);
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
        return this.getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
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
        this.getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    public void sendBlock(Player player, Vector3i blockCoordinates) {
        this.sendBlock(player, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    public void sendBlock(Player player, int x, int y, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunk(chunkX, chunkZ).sendBlock(player, x % 16, y, z % 16);
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

        this.entities.put(entity.getId(), entity);
        if (entity instanceof Player) {
            this.players.add((Player) entity);
        }

        ImplEntity implEntity = (ImplEntity) entity;
        implEntity.setLocation(location);
        ((ImplChunk) location.getChunk()).addEntity(implEntity);
        implEntity.onSpawned();
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!this.equals(entity.getWorld())) {
            throw new IllegalStateException("This entity has not been spawned in this world");
        }

        this.entities.remove(entity.getId());
        if (entity instanceof Player) {
            this.players.remove(entity);
        }

        ImplEntity implEntity = (ImplEntity) entity;
        ImplChunk chunk = implEntity.getChunk();

        // Remove the location first to signify that the entity is gone and not just moving to another chunk.
        implEntity.setLocation(null);   // the entity no longer exists in any world
        implEntity.onDespawned();

        chunk.removeEntity(implEntity);
    }

    @Override
    public Map<Long, Entity> getEntities() {
        return Collections.unmodifiableMap(this.entities);
    }

    @Override
    public Optional<Entity> getEntity(long id) {
        return Optional.ofNullable(this.entities.getOrDefault(id, null));
    }

    @Override
    public void tick() {
        this.chunkManager.tick();
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
        this.getServer().getEventManager().call(event);
        if (!event.isCancelled()) {
            for (Player player : this.getPlayers()) {
                player.sendPacket(packet);
            }
        }
    }

    /**
     * Retrieve the default save data for a player spawning in this world.
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
        this.chunkManager.close();
    }

    @Override
    public int hashCode() {
        return 43 + (43 * this.getDimension().hashCode()) + (43 * this.getLevel().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplWorld) {
            ImplWorld otherWorld = (ImplWorld) obj;
            return otherWorld.getDimension().equals(this.getDimension()) && otherWorld.getLevel().equals(this.getLevel());
        }
        return false;
    }

    private static int getChunkCoordinate(int i) {
        return (int) Math.floor(i / 16d);
    }

}
