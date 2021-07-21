package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelChunkPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateBlockPacket;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.World;
import io.github.willqi.pizzaserver.server.world.blocks.Block;
import io.github.willqi.pizzaserver.server.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Chunk {

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


    protected Chunk(World world, int x, int z, List<BedrockSubChunk> subChunks, byte[] biomeData) {
        if (subChunks.size() != 16) {
            throw new IllegalArgumentException("Tried to construct chunk with only " + subChunks.size() + " subchunks instead of 16.");
        }

        this.world = world;
        this.x = x;
        this.z = z;
        this.subChunks = subChunks;
        this.biomeData = biomeData;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public Set<Entity> getEntities() {
        return this.entities;
    }

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    public Block getBlock(Vector3i blockPosition) {
        return this.getBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    public Block getBlock(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) {
            throw new IllegalArgumentException("Could not retrieve block outside chunk");
        }
        int subChunkIndex = y / 16;
        int blockIndex = getBlockCacheIndex(x, y, z); // Index stored in chunk block cache

        Lock readLock = this.lock.readLock();
        readLock.lock();

        if (!this.cachedBlocks.containsKey(subChunkIndex)) {
            this.cachedBlocks.put(subChunkIndex, new HashMap<>());
        }
        Map<Integer, Block> subChunkCache = this.cachedBlocks.get(subChunkIndex);

        if (subChunkCache.containsKey(blockIndex)) {
            return subChunkCache.get(blockIndex);
        }

        // Construct new block as none is cached
        BlockPalette.Entry paletteEntry = this.subChunks.get(subChunkIndex).getLayer(0).getBlockEntryAt(x, y % 16, z);
        BlockRegistry blockRegistry = this.getWorld().getServer().getBlockRegistry();
        Block block;
        if (blockRegistry.hasBlockType(paletteEntry.getId())) {
            // Block id is registered
            BlockType blockType = blockRegistry.getBlockType(paletteEntry.getId());
            block = new Block(blockType);
            block.setBlockStateIndex(blockType.getBlockStateIndex(paletteEntry.getState()));
        } else {
            // The block id is not registered
            this.getWorld().getServer().getLogger().warn("Could not find block type for id " + paletteEntry.getId() + ". Substituting with air");
            BlockType blockType = blockRegistry.getBlockType(BlockTypeID.AIR);
            block = new Block(blockType);
        }
        subChunkCache.put(blockIndex, block);

        readLock.unlock();
        return block;
    }

    public void setBlock(BlockType blockType, Vector3i blockPosition) {
        this.setBlock(new Block(blockType), blockPosition);
    }

    public void setBlock(Block block, Vector3i blockPosition) {
        this.setBlock(block, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    public void setBlock(Block block, int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) {
            throw new IllegalArgumentException("Could not retrieve block outside chunk");
        }
        int subChunkIndex = y / 16;
        int blockIndex = getBlockCacheIndex(x, y, z); // Index stored in chunk block cache

        Lock writeLock = this.lock.writeLock();
        writeLock.lock();

        if (!this.cachedBlocks.containsKey(subChunkIndex)) {
            this.cachedBlocks.put(subChunkIndex, new HashMap<>());
        }
        Map<Integer, Block> subChunkCache = this.cachedBlocks.get(subChunkIndex);
        subChunkCache.put(blockIndex, block);

        // Update internal sub chunk
        BedrockSubChunk subChunk = this.subChunks.get(subChunkIndex);
        BlockLayer mainBlockLayer = subChunk.getLayer(0);
        BlockPalette.Entry entry = mainBlockLayer.getPalette().create(block.getBlockType().getBlockId(), block.getBlockState(), ServerProtocol.LATEST_BLOCK_STATES_VERSION);
        mainBlockLayer.setBlockEntryAt(x, y, z, entry);

        // Send update block packet
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlock(block);
        updateBlockPacket.setBlockCoordinates(new Vector3i(x, y, z));
        updateBlockPacket.setLayer(0);
        updateBlockPacket.setFlags(Collections.singleton(UpdateBlockPacket.Flag.NETWORK));
        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(updateBlockPacket);
        }

        writeLock.unlock();
    }

    private static int getBlockCacheIndex(int x, int y, int z) {
        return (x * 256) + (z * 16) + y; // Index stored in chunk block cache
    }

    /**
     * Send the chunk blocks to a player
     * It is recommended that this is done async
     * @param player
     */
    public void sendBlocksTo(Player player) {
        Lock readLock = this.lock.writeLock();
        readLock.lock();

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
                Server.getInstance().getLogger().error("Failed to serialize subchunk (x: " + this.getX() + " z: " + this.getZ() + " index: " + subChunkCount + ")");
                return;
            }
        }
        packetData.writeBytes(this.getBiomeData());
        packetData.writeByte(0);    // edu feature or smth

        byte[] data = new byte[packetData.readableBytes()];
        packetData.readBytes(data);
        packetData.release();

        // TODO: Supposedly tile entities are also packaged here
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.setX(this.getX());
        levelChunkPacket.setZ(this.getZ());
        levelChunkPacket.setSubChunkCount(subChunkCount);
        levelChunkPacket.setData(data);
        player.sendPacket(levelChunkPacket);

        this.spawnedTo.add(player);
        readLock.unlock();
    }

    /**
     * Send the entities of this chunk to a player
     * This should only be called on the MAIN thread
     * @param player
     */
    public void sendEntitiesTo(Player player) {
        for (Entity entity : this.getEntities()) {
            entity.spawnTo(player);
        }

        this.spawnedTo.add(player);
    }

    public Set<Player> getViewers() {
        return new HashSet<>(this.spawnedTo);
    }

    /**
     * Called when a player can no longer see a chunk
     * Despawns all entities in this chunk from the player's view
     * @param player
     */
    public void despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            for (Entity entity : this.getEntities()) {
                entity.despawnFrom(player);
            }

            // Should we close this chunk
            if (this.getViewers().size() == 0) {
                this.getWorld().getChunkManager().unloadChunk(this.getX(), this.getZ());
            }
        }
    }

    public void close() {}

    @Override
    public int hashCode() {
        return 7 + (37 * this.getX()) + (37 * this.getZ()) + (37 * this.getWorld().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chunk) {
            Chunk otherChunk = (Chunk)obj;
            return (otherChunk.getX() == this.getX()) && (otherChunk.getZ() == this.getZ()) && (otherChunk.getWorld().equals(this.getWorld()));
        }
        return false;
    }


    public static class Builder {

        private List<BedrockSubChunk> subChunks = Collections.emptyList();
        private byte[] biomeData = new byte[256];

        private World world;
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

        public Builder setWorld(World world) {
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

        public Chunk build() {
            return new Chunk(this.world, this.x, this.z, this.subChunks, this.biomeData);
        }


    }

}
