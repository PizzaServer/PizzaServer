package io.github.pizzaserver.server.level.world;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import com.nukkitx.protocol.bedrock.packet.LevelSoundEventPacket;
import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.event.type.world.WorldSoundEvent;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.level.world.chunks.loader.ChunkLoader;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.api.utils.Location;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.level.ImplLevel;
import io.github.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.pizzaserver.server.level.world.chunks.WorldChunkManager;
import io.github.pizzaserver.server.player.playerdata.PlayerData;

import java.io.IOException;
import java.util.*;

public class ImplWorld implements World {

    protected final ImplLevel level;
    protected final Dimension dimension;
    protected final WorldChunkManager chunkManager = new WorldChunkManager(this);

    protected Vector3i spawnCoordinates;
    protected int time;

    protected final Set<Player> players = new HashSet<>();
    protected final Map<Long, Entity> entities = new HashMap<>();


    public ImplWorld(ImplLevel level, Dimension dimension) {
        this.level = level;
        this.dimension = dimension;

        Vector3i worldSpawnCoordinates = this.level.getProvider().getLevelData().getWorldSpawn();
        if (worldSpawnCoordinates.getY() > 255) {    // Hack to get around Minecraft worlds having REALLY high y spawn coordinates in the level.dat
            worldSpawnCoordinates = Vector3i.from(worldSpawnCoordinates.getX(),
                    this.getHighestBlockAt(worldSpawnCoordinates.getX(), worldSpawnCoordinates.getZ()).getY(),
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
    public Block getHighestBlockAt(Vector2i coordinates) {
        return this.getHighestBlockAt(coordinates.getX(), coordinates.getY());
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);

        return this.getChunk(chunkX, chunkZ).getHighestBlockAt(x & 15, z & 15);
    }

    @Override
    public Block getBlock(int x, int y, int z, int layer) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);

        return this.getChunk(chunkX, chunkZ).getBlock(x & 15, y, z & 15, layer);
    }

    @Override
    public Optional<BlockEntity> getBlockEntity(int x, int y, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        return this.getChunk(chunkX, chunkZ).getBlockEntity(x & 15, y, z & 15);
    }

    @Override
    public void setBlock(Block block, int x, int y, int z, int layer) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunk(chunkX, chunkZ).setBlock(block, x & 15, y, z & 15, layer);
    }

    @Override
    public void setAndUpdateBlock(Block block, int x, int y, int z, int layer) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunk(chunkX, chunkZ).setAndUpdateBlock(block, x & 15, y, z & 15, layer);
    }

    @Override
    public boolean requestBlockUpdate(BlockUpdateType type, int x, int y, int z, int ticks) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        return this.getChunk(chunkX, chunkZ).requestBlockUpdate(type, x & 15, y, z & 15, ticks);
    }

    @Override
    public void addBlockEvent(int x, int y, int z, int type, int data) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunk(chunkX, chunkZ).addBlockEvent(x & 15, y, z & 15, type, data);
    }

    @Override
    public void addItemEntity(ItemStack itemStack, Vector3f position) {
        this.addItemEntity(itemStack, position, Vector3f.ZERO);
    }

    @Override
    public void addItemEntity(ItemStack itemStack, Vector3f position, Vector3f velocity) {
        this.addItemEntity(this.getServer().getEntityRegistry().getItemEntity(itemStack), position, velocity);
    }

    @Override
    public void addItemEntity(EntityItem itemEntity, Vector3f position) {
        this.addItemEntity(itemEntity, position, itemEntity.getMotion());
    }

    @Override
    public void addItemEntity(EntityItem itemEntity, Vector3f position, Vector3f motion) {
        itemEntity.setMotion(motion);
        if (!itemEntity.getItem().isEmpty()) {
            this.addEntity(itemEntity, position);
        }
    }

    public void sendBlock(Player player, Vector3i blockCoordinates) {
        this.sendBlock(player, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    public void sendBlock(Player player, int x, int y, int z) {
        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        this.getChunk(chunkX, chunkZ).sendBlock(player, x & 15, y, z & 15);
    }

    @Override
    public void addEntity(Entity entity, Vector3f position) {
        if (entity.hasSpawned()) {
            throw new IllegalArgumentException("The provided entity was already spawned in a world.");
        }
        Location location = new Location(this, position);

        this.entities.put(entity.getId(), entity);
        if (entity instanceof Player) {
            this.players.add((Player) entity);
        }

        ImplEntity implEntity = (ImplEntity) entity;
        implEntity.setPosition(location);
        ((ImplChunk) location.getChunk()).addEntity(implEntity);
        implEntity.onSpawned();
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!this.entities.containsKey(entity.getId())) {
            throw new IllegalStateException("This entity has not been spawned in this world");
        }

        this.entities.remove(entity.getId());
        if (entity instanceof Player) {
            this.players.remove(entity);
        }

        ImplEntity implEntity = (ImplEntity) entity;
        ImplChunk chunk = implEntity.getChunk();

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
    public Set<Entity> getEntitiesNear(Vector3f position, int maxDistance) {
        Set<Entity> entities = new HashSet<>();

        int minChunkX = getChunkCoordinate(position.getFloorX() - maxDistance);
        int maxChunkX = getChunkCoordinate(position.getFloorX() + maxDistance);
        int minChunkZ = getChunkCoordinate(position.getFloorZ() - maxDistance);
        int maxChunkZ = getChunkCoordinate(position.getFloorZ() + maxDistance);

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                Chunk chunk = this.getChunk(chunkX, chunkZ);

                for (Entity entity : chunk.getEntities()) {
                    if (entity.getLocation().toVector3f().distance(position) <= maxDistance) {
                        entities.add(entity);
                    }
                }
            }
        }

        return entities;
    }

    @Override
    public void tick() {
        this.chunkManager.tick();
        this.time++;
    }

    @Override
    public boolean isDay() {
        int time = this.getTime() % 24000;
        return time < 12000;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public void setTime(int time) {
        this.time = time >= 0 ? time : 24000 + time;

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.setTime(time);
        for (Player player : this.getPlayers()) {
            player.sendPacket(setTimePacket);
        }
    }

    @Override
    public void playSound(SoundEvent sound, Vector3f position, boolean relativeVolumeDisabled, boolean isBaby, String entityType, Block block) {
        WorldSoundEvent event = new WorldSoundEvent(new Location(this, position), sound, relativeVolumeDisabled, isBaby, entityType, block);
        this.getServer().getEventManager().call(event);

        if (!event.isCancelled()) {
            for (Player player : this.getPlayers()) {
                LevelSoundEventPacket packet = new LevelSoundEventPacket();
                packet.setSound(sound);
                packet.setPosition(position);
                packet.setRelativeVolumeDisabled(relativeVolumeDisabled);
                packet.setBabySound(isBaby);
                packet.setIdentifier(entityType);
                packet.setExtraData(block != null ? player.getVersion().getBlockRuntimeId(block.getBlockId(), block.getNBTState()) : -1);

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
                .setLevelName(this.getLevel().getProvider().getFile().getName())
                .setDimension(this.getDimension())
                .setGamemode(Gamemode.SURVIVAL)
                .setPosition(this.getSpawnCoordinates().add(0, 2, 0).toFloat())
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
