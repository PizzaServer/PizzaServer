package io.github.willqi.pizzaserver.server.level.world.chunks;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldChunkPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateBlockPacket;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a 16x16 chunk of blocks on the server
 */
public class ImplChunk implements Chunk {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final List<BedrockSubChunk> subChunks;
    private final byte[] biomeData;

    // subChunkIndex : ( blockIndex : block )
    private final Map<Integer, Map<Integer, Block>> cachedBlocks = new HashMap<>();

    private final World world;
    private final int x;
    private final int z;

    // Entities in this chunk
    private final Set<Entity> entities = new HashSet<>();

    // The players who can see this chunk
    private final Set<Player> spawnedTo = ConcurrentHashMap.newKeySet();


    protected ImplChunk(World world, int x, int z, List<BedrockSubChunk> subChunks, byte[] biomeData) {
        if (subChunks.size() != 16) {
            throw new IllegalArgumentException("Tried to construct chunk with only " + subChunks.size() + " subchunks instead of 16.");
        }

        this.world = world;
        this.x = x;
        this.z = z;
        this.subChunks = subChunks;
        this.biomeData = biomeData;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public boolean canBeVisibleTo(Player player) {
        return (player.getChunkRadius() + player.getLocation().getChunkX() >= this.getX()) && (player.getLocation().getChunkX() - player.getChunkRadius() <= this.getX()) &&
                (player.getChunkRadius() + player.getLocation().getChunkZ() >= this.getZ()) && (player.getLocation().getChunkZ() - player.getChunkRadius() <= this.getZ());
    }

    @Override
    public byte getBiomeAt(int x, int z) {
        if (x > 15 || z > 15 || x < 0 || z < 0) {
            throw new IllegalArgumentException("Could not fetch biome of block outside of chunk");
        }
        return this.biomeData[z * 16 + x];
    }

    /**
     * Add this entity to be spawned for viewers of this chunk
     * Called when the entity is in this chunk
     * @param entity the entity to spawn
     */
    public void addEntity(Entity entity) {
        this.entities.add(entity);
        for (Player player : this.getViewers()) {
            // an entity cannot be spawned to a player if it is the entity,
            // has already been spawned, or if the entity is hidden from the player.
            boolean canSpawnToPlayer = !entity.equals(player) &&
                    !entity.hasSpawnedTo(player) &&
                    (!(entity instanceof LivingEntity) || !((LivingEntity)entity).isHiddenFrom(player));

            if (canSpawnToPlayer) {
                entity.spawnTo(player);
            }
        }
    }

    /**
     * Remove this entity from the set of entities that are spawned to viewers
     * Called when the entity is moved to another chunk or is despawned
     * @param entity the entity to spawn
     */
    public void removeEntity(Entity entity) {
        if (this.entities.remove(entity)) {
            for (Player player : this.getViewers()) {
                // Entities are despawned from a viewer if it no longer has a location or if
                // it is not visible to the entity in its new location.
                if ((entity.getChunk() == null || !entity.getChunk().canBeVisibleTo(player)) && entity.hasSpawnedTo(player)) {
                    entity.despawnFrom(player);
                }
            }
        }
    }

    @Override
    public Set<Entity> getEntities() {
        return new HashSet<>(this.entities);
    }

    private byte[] getBiomeData() {
        return this.biomeData;
    }

    @Override
    public Block getBlock(Vector3i blockPosition) {
        return this.getBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (y >= 256 || y < 0 || Math.abs(x) >= 16 || Math.abs(z) >= 16) {
            throw new IllegalArgumentException("Could not change block outside chunk");
        }
        int subChunkIndex = y / 16;
        int chunkBlockX = x >= 0 ? x : 16 + x;
        int chunkBlockY = y % 16;
        int chunkBlockZ = z >= 0 ? z : 16 + z;
        int blockIndex = getBlockCacheIndex(chunkBlockX, chunkBlockY, chunkBlockZ); // Index stored in chunk block cache

        Lock readLock = this.lock.readLock();
        readLock.lock();

        try {
            if (!this.cachedBlocks.containsKey(subChunkIndex)) {
                this.cachedBlocks.put(subChunkIndex, new HashMap<>());
            }
            Map<Integer, Block> subChunkCache = this.cachedBlocks.get(subChunkIndex);

            if (subChunkCache.containsKey(blockIndex)) {
                return subChunkCache.get(blockIndex);
            }

            // Construct new block as none is cached
            BlockPalette.Entry paletteEntry = this.subChunks.get(subChunkIndex).getLayer(0).getBlockEntryAt(chunkBlockX, chunkBlockY, chunkBlockZ);
            BlockRegistry blockRegistry = this.getWorld().getServer().getBlockRegistry();
            Block block;
            if (blockRegistry.hasBlockType(paletteEntry.getId())) {
                // Block id is registered
                BaseBlockType blockType = blockRegistry.getBlockType(paletteEntry.getId());
                block = new Block(blockType);
                block.setBlockStateIndex(blockType.getBlockStateIndex(paletteEntry.getState()));
            } else {
                // The block id is not registered
                this.getWorld().getServer().getLogger().warn("Could not find block type for id " + paletteEntry.getId() + ". Substituting with air");
                BaseBlockType blockType = blockRegistry.getBlockType(BlockTypeID.AIR);
                block = new Block(blockType);
            }
            subChunkCache.put(blockIndex, block);
            return block;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setBlock(BaseBlockType blockType, Vector3i blockPosition) {
        this.setBlock(new Block(blockType), blockPosition);
    }

    @Override
    public void setBlock(BaseBlockType blockType, int x, int y, int z) {
        this.setBlock(new Block(blockType), x, y, z);
    }

    @Override
    public void setBlock(Block block, Vector3i blockPosition) {
        this.setBlock(block, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public void setBlock(Block block, int x, int y, int z) {
        if (y >= 256 || y < 0 || Math.abs(x) >= 16 || Math.abs(z) >= 16) {
            throw new IllegalArgumentException("Could not change block outside chunk");
        }
        int subChunkIndex = y / 16;
        int chunkBlockX = x >= 0 ? x : 16 + x;
        int chunkBlockY = y % 16;
        int chunkBlockZ = z >= 0 ? z : 16 + z;
        int blockIndex = getBlockCacheIndex(chunkBlockX, chunkBlockY, chunkBlockZ); // Index stored in chunk block cache

        Lock writeLock = this.lock.writeLock();
        writeLock.lock();

        try {
            if (!this.cachedBlocks.containsKey(subChunkIndex)) {
                this.cachedBlocks.put(subChunkIndex, new HashMap<>());
            }
            Map<Integer, Block> subChunkCache = this.cachedBlocks.get(subChunkIndex);
            subChunkCache.put(blockIndex, block);

            // Update internal sub chunk
            BedrockSubChunk subChunk = this.subChunks.get(subChunkIndex);
            BlockLayer mainBlockLayer = subChunk.getLayer(0);
            BlockPalette.Entry entry = mainBlockLayer.getPalette().create(block.getBlockType().getBlockId(), block.getBlockState(), ServerProtocol.LATEST_BLOCK_STATES_VERSION);
            mainBlockLayer.setBlockEntryAt(chunkBlockX, chunkBlockY, chunkBlockZ, entry);

            // Send update block packet
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setBlock(block);
            updateBlockPacket.setBlockCoordinates(new Vector3i(this.getX() * 16 + x, y, this.getZ() * 16 + z));
            updateBlockPacket.setLayer(0);
            updateBlockPacket.setFlags(Collections.singleton(UpdateBlockPacket.Flag.NETWORK));
            for (Player viewer : this.getViewers()) {
                viewer.sendPacket(updateBlockPacket);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * run block updates and tick entities in this chunk
     */
    public void tick() {
        for (Entity entity : this.getEntities()) {
            entity.tick();
        }
    }

    @Override
    public void sendTo(Player player) {
        this.sendBlocksTo(player);
        this.getWorld().getServer().getScheduler().prepareTask(() -> {
            this.sendEntitiesTo(player);
        }).schedule();
    }

    /**
     * Retrieve the index to store a block in the child map in the cachedBlocks map
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return unique hash for the coordinate
     */
    private static int getBlockCacheIndex(int x, int y, int z) {
        return (x * 256) + (z * 16) + y; // Index stored in chunk block cache
    }

    /**
     * Send the chunk blocks to a {@link Player}
     * It is recommended that this is done async as it can take a while to serialize.
     * @param player the {@link Player} to send it to
     */
    public void sendBlocksTo(Player player) {
        Lock readLock = this.lock.writeLock();
        readLock.lock();

        try {
            // Find the lowest from the top empty subchunk
            int subChunkCount = this.subChunks.size() - 1;
            for (; subChunkCount >= 0; subChunkCount--) {
                BedrockSubChunk subChunk = this.subChunks.get(subChunkCount);
                if (subChunk.getLayers().size() > 0) {
                    break;
                }
            }
            subChunkCount++;

            // Write all subchunks
            ByteBuf packetData = ByteBufAllocator.DEFAULT.buffer();
            for (int subChunkIndex = 0; subChunkIndex < subChunkCount; subChunkIndex++) {
                BedrockSubChunk subChunk = this.subChunks.get(subChunkIndex);
                try {
                    byte[] subChunkSerialized = subChunk.serializeForNetwork(player.getVersion());
                    packetData.writeBytes(subChunkSerialized);
                } catch (IOException exception) {
                    ImplServer.getInstance().getLogger().error("Failed to serialize subchunk (x: " + this.getX() + " z: " + this.getZ() + " index: " + subChunkCount + ")");
                    return;
                }
            }
            packetData.writeBytes(this.getBiomeData());
            packetData.writeByte(0);    // edu feature or smth

            byte[] data = new byte[packetData.readableBytes()];
            packetData.readBytes(data);
            packetData.release();

            this.spawnedTo.add(player);

            // Packets are sent on the main thread
            final int packetSubChunkCount = subChunkCount;
            this.getWorld().getServer().getScheduler().prepareTask(() -> {
                if (player.getLocation().getWorld().equals(this.getWorld())) {
                    // TODO: Supposedly tile entities are also packaged here
                    WorldChunkPacket worldChunkPacket = new WorldChunkPacket();
                    worldChunkPacket.setX(this.getX());
                    worldChunkPacket.setZ(this.getZ());
                    worldChunkPacket.setSubChunkCount(packetSubChunkCount);
                    worldChunkPacket.setData(data);
                    player.sendPacket(worldChunkPacket);
                }
            }).schedule();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Send the {@link BaseEntity}s of this chunk to a {@link Player}
     * This should only be called on the MAIN thread
     * @param player
     */
    public void sendEntitiesTo(Player player) {
        for (Entity entity : this.getEntities()) {
            if (player.equals(entity) || (entity instanceof LivingEntity && ((LivingEntity)entity).isHiddenFrom(player))) {
                continue;
            }
            entity.spawnTo(player);
        }

        this.spawnedTo.add(player);
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.spawnedTo);
    }

    public void despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            for (Entity entity : this.getEntities()) {
                if (!entity.equals(player)) {
                    entity.despawnFrom(player);
                }
            }

            if (this.canBeClosed()) {
                // Attempt to unload this chunk
                this.getWorld().getChunkManager().unloadChunk(this.getX(), this.getZ(), true, false);
            }
        }
    }

    @Override
    public boolean canBeClosed() {
        return this.spawnedTo.size() == 0;
    }

    @Override
    public void close() {}

    @Override
    public int hashCode() {
        return 7 + (37 * this.getX()) + (37 * this.getZ()) + (37 * this.getWorld().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplChunk) {
            ImplChunk otherChunk = (ImplChunk)obj;
            return (otherChunk.getX() == this.getX()) && (otherChunk.getZ() == this.getZ()) && (otherChunk.getWorld().equals(this.getWorld()));
        }
        return false;
    }


    public static class Builder {

        private List<BedrockSubChunk> subChunks = Collections.emptyList();
        private byte[] biomeData = new byte[256];

        private ImplWorld world;
        private int x;
        private int z;


        public Builder setSubChunks(List<BedrockSubChunk> subChunks) {
            this.subChunks = subChunks;
            return this;
        }

        public Builder setBiomeData(byte[] biomeData) {
            this.biomeData = biomeData;
            return this;
        }

        public Builder setWorld(ImplWorld world) {
            this.world = world;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setZ(int z) {
            this.z = z;
            return this;
        }

        public ImplChunk build() {
            Check.nullParam(this.world, "world");
            return new ImplChunk(this.world, this.x, this.z, this.subChunks, this.biomeData);
        }


    }

}