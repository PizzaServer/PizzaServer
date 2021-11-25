package io.github.pizzaserver.server.level.world.chunks;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.pizzaserver.format.api.chunks.subchunks.BedrockSubChunk;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockLayer;
import io.github.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.api.level.world.blocks.types.BlockTypeID;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a 16x16 chunk of blocks on the server.
 */
public class ImplChunk implements Chunk {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final BedrockChunk chunk;

    private final ImplWorld world;
    private final int x;
    private final int z;

    private final List<Vector3i> blockUpdates = new ArrayList<>();

    private int expiryTimer;
    private final AtomicInteger activeChunkLoaders = new AtomicInteger(0);

    // Entities in this chunk
    private final Set<Entity> entities = new HashSet<>();

    // The players who can see this chunk
    private final Set<Player> spawnedTo = ConcurrentHashMap.newKeySet();


    protected ImplChunk(ImplWorld world, int x, int z, BedrockChunk chunk) {
        if (chunk.getSubChunks().size() != 16) {
            throw new IllegalArgumentException("Tried to construct chunk with only " + chunk.getSubChunks().size() + " subchunks instead of 16.");
        }

        this.world = world;
        this.x = x;
        this.z = z;
        this.chunk = chunk;
        this.resetExpiryTime();
    }

    @Override
    public ImplWorld getWorld() {
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
        return (player.getChunkRadius() + player.getLocation().getChunkX() >= this.getX()) && (player.getLocation().getChunkX() - player.getChunkRadius() <= this.getX())
                && (player.getChunkRadius() + player.getLocation().getChunkZ() >= this.getZ()) && (player.getLocation().getChunkZ() - player.getChunkRadius() <= this.getZ());
    }

    @Override
    public byte getBiomeAt(int x, int z) {
        if (x > 15 || z > 15 || x < 0 || z < 0) {
            throw new IllegalArgumentException("Could not fetch biome of block outside of chunk");
        }
        return this.chunk.getBiomeAt(x, z);
    }

    /**
     * Add this entity this chunk.
     * The entity is also spawned to any viewers of this chunk within render distance.
     * @param entity the entity to spawn
     */
    public void addEntity(ImplEntity entity) {
        if (!this.entities.contains(entity)) {
            for (Player player : this.getViewers()) {
                if (entity.canBeSpawnedTo(player)) {
                    entity.spawnTo(player);
                }
            }

            this.entities.add(entity);
        }
    }

    /**
     * Remove this entity from this chunk.
     * The entity is also despawned from any viewers of this chunk who are no longer within render distance.
     * @param entity the entity to spawn
     */
    public void removeEntity(Entity entity) {
        if (this.entities.remove(entity)) {
            for (Player player : this.getViewers()) {
                if (!player.equals(entity)) {
                    if (((ImplEntity) entity).shouldBeDespawnedFrom(player)) {
                        entity.despawnFrom(player);
                    }
                }
            }
        }
    }

    @Override
    public Set<Entity> getEntities() {
        return new HashSet<>(this.entities);
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        int chunkBlockY = Math.max(0, this.chunk.getHighestBlockAt(x & 15, z & 15) - 1);
        return this.getBlock(x, chunkBlockY, z);
    }

    @Override
    public Block getBlock(Vector3i blockCoordinates, int layer) {
        return this.getBlock(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    @Override
    public Block getBlock(int x, int y, int z, int layer) {
        if (y >= 256 || y < 0 || Math.abs(x) >= 16 || Math.abs(z) >= 16) {
            throw new IllegalArgumentException("Could not get block outside chunk");
        }
        int subChunkIndex = y / 16;
        int chunkBlockX = x & 15;
        int subChunkY = y & 15;
        int chunkBlockZ = z & 15;
        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + chunkBlockX, y, this.getZ() * 16 + chunkBlockZ);

        Lock readLock = this.lock.readLock();
        readLock.lock();

        try {
            BedrockSubChunk subChunk = this.chunk.getSubChunk(subChunkIndex);
            if (subChunk.getLayers().size() <= layer) {
                // layer does not exist: return air block
                Block block = BlockRegistry.getBlock(BlockTypeID.AIR);
                block.setLocation(this.getWorld(), blockCoordinates);
                return block;
            }
            BlockPalette.Entry paletteEntry = subChunk.getLayer(layer).getBlockEntryAt(chunkBlockX, subChunkY, chunkBlockZ);
            Block block;
            if (BlockRegistry.hasBlockType(paletteEntry.getId())) {
                // Block id is registered
                BaseBlockType blockType = BlockRegistry.getBlockType(paletteEntry.getId());
                block = new Block(blockType);
                block.setBlockStateIndex(blockType.getBlockStateIndex(paletteEntry.getState()));
            } else {
                // The block id is not registered
                this.getWorld().getServer().getLogger().warn("Could not find block type for id " + paletteEntry.getId() + ". Substituting with air");
                BaseBlockType blockType = BlockRegistry.getBlockType(BlockTypeID.AIR);
                block = new Block(blockType);
            }
            block.setLocation(this.getWorld(), blockCoordinates);

            return block;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setBlock(Block block, int x, int y, int z, int layer) {
        if (y >= 256 || y < 0 || Math.abs(x) >= 16 || Math.abs(z) >= 16) {
            throw new IllegalArgumentException("Could not change block outside chunk");
        }
        int subChunkIndex = y / 16;
        int chunkBlockX = x & 15;
        int subChunkBlockY = y & 15;
        int chunkBlockZ = z & 15;
        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + chunkBlockX, y, this.getZ() * 16 + chunkBlockZ);

        block.setLocation(this.getWorld(), blockCoordinates);

        Lock writeLock = this.lock.writeLock();
        writeLock.lock();

        try {
            // Update internal sub chunk
            BedrockSubChunk subChunk = this.chunk.getSubChunks().get(subChunkIndex);
            BlockLayer mainBlockLayer = subChunk.getLayer(layer);
            BlockPalette.Entry entry = mainBlockLayer.getPalette().create(block.getBlockType().getBlockId(), block.getBlockState().getNBT(), ServerProtocol.LATEST_BLOCK_STATES_VERSION);
            mainBlockLayer.setBlockEntryAt(chunkBlockX, subChunkBlockY, chunkBlockZ, entry);

            int highestBlockY = Math.max(0, this.chunk.getHighestBlockAt(chunkBlockX, chunkBlockZ) - 1);
            if (y >= highestBlockY) {
                int newHighestBlockY = y;
                while (this.getBlock(chunkBlockX, newHighestBlockY, chunkBlockZ).getBlockState().isAir()) {
                    newHighestBlockY--;
                }
                this.chunk.setHighestBlockAt(chunkBlockX, chunkBlockZ, newHighestBlockY + 1);
            }

            // Send update block packet
            for (Player viewer : this.getViewers()) {
                this.sendBlock(viewer, chunkBlockX, y, chunkBlockZ, layer);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void setAndUpdateBlock(Block block, int x, int y, int z, int layer) {
        this.setBlock(block, x, y, z, layer);
        int chunkBlockX = x & 15;
        int chunkBlockZ = z & 15;

        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + chunkBlockX, y, this.getZ() * 16 + chunkBlockZ);

        this.getWorld().requestBlockUpdate(blockCoordinates.up());
        this.getWorld().requestBlockUpdate(blockCoordinates.down());
        this.getWorld().requestBlockUpdate(blockCoordinates.north());
        this.getWorld().requestBlockUpdate(blockCoordinates.south());
        this.getWorld().requestBlockUpdate(blockCoordinates.west());
        this.getWorld().requestBlockUpdate(blockCoordinates.east());
    }

    @Override
    public boolean requestBlockUpdate(int x, int y, int z) {
        Vector3i blockCoordinates = Vector3i.from(x & 15, y, z & 15);
        if (!this.blockUpdates.contains(blockCoordinates)) {
            this.blockUpdates.add(blockCoordinates);
            return true;
        }
        return false;
    }

    private void doBlockUpdate(int x, int y, int z) {
        int subChunkIndex = y / 16;
        int chunkBlockX = x & 15;
        int chunkBlockZ = z & 15;

        int layers = Math.max(this.chunk.getSubChunk(subChunkIndex).getLayers().size(), 1);

        for (int layer = 0; layer < layers; layer++) {
            Block block = this.getBlock(chunkBlockX, y, chunkBlockZ, layer);
            block.getBlockType().onUpdate(block);
        }
    }

    /**
     * Send all layers of a block to the client.
     * @param player the player being sent the layers
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public void sendBlock(Player player, int x, int y, int z) {
        int subChunkIndex = y / 16;

        // Ensure that at least the foremost layer is sent to the client
        int layers = Math.max(this.chunk.getSubChunk(subChunkIndex).getLayers().size(), 1);

        for (int layer = 0; layer < layers; layer++) {
            this.sendBlock(player, x & 15, y, z & 15, layer);
        }
    }

    /**
     * Send a layer of a block to the client.
     * @param player the player being sent the layer
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    public void sendBlock(Player player, int x, int y, int z, int layer) {
        int chunkBlockX = x & 15;
        int chunkBlockZ = z & 15;

        Block block = this.getBlock(chunkBlockX, y, chunkBlockZ, layer);
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setRuntimeId(player.getVersion().getBlockRuntimeId(block.getBlockType().getBlockId(), block.getBlockState().getNBT()));
        updateBlockPacket.setBlockPosition(Vector3i.from(chunkBlockX + this.getX() * 16, y, chunkBlockZ + this.getZ() * 16));
        updateBlockPacket.setDataLayer(layer);
        updateBlockPacket.getFlags().add(UpdateBlockPacket.Flag.NETWORK);
        player.sendPacket(updateBlockPacket);
    }

    /**
     * run block updates and tick entities in this chunk.
     */
    public void tick() {
        boolean canDoLogicTick = false;
        for (Player player : this.getViewers()) {
            int chunkDistance = (int) Math.floor(Math.sqrt(Math.pow(player.getChunk().getX() - this.getX(), 2) + Math.pow(player.getChunk().getZ() - this.getZ(), 2)));
            if (chunkDistance <= this.getWorld().getServer().getConfig().getChunkPlayerTickRadius()) {
                canDoLogicTick = true;
                break;
            }
        }

        if (canDoLogicTick) {
            for (Entity entity : this.getEntities()) {
                entity.tick();
            }

            List<Vector3i> currentTickBlockUpdates = new ArrayList<>(this.blockUpdates);
            this.blockUpdates.clear();
            for (Vector3i blockCoordinate : currentTickBlockUpdates) {
                this.doBlockUpdate(blockCoordinate.getX(), blockCoordinate.getY(), blockCoordinate.getZ());
            }
        }

        if (this.expiryTimer > 0 && this.canBeClosed()) {
            this.expiryTimer--;

            if (this.expiryTimer == 0) {
                this.close(true, false);
            }
        }
    }

    public void spawnTo(Player player) {
        // Send the blocks to the player
        Lock readLock = this.lock.writeLock();
        readLock.lock();
        try {
            int subChunkCount = this.chunk.getSubChunkCount();
            byte[] data = this.chunk.serializeForNetwork(player.getVersion());

            // Ran on the main thread in order because player.getLocation() is not thread safe
            final int packetSubChunkCount = subChunkCount;
            this.getWorld().getServer().getScheduler().prepareTask(() -> {
                if (player.isConnected() && player.getLocation().getWorld().equals(this.getWorld())) {
                    // TODO: Supposedly tile entities are also packaged here
                    LevelChunkPacket chunkPacket = new LevelChunkPacket();
                    chunkPacket.setChunkX(this.getX());
                    chunkPacket.setChunkZ(this.getZ());
                    chunkPacket.setSubChunksLength(packetSubChunkCount);
                    chunkPacket.setData(data);
                    player.sendPacket(chunkPacket);

                    this.spawnedTo.add(player);
                    this.resetExpiryTime();
                }

                // Send the entities of this chunk to the player
                if (player.isLocallyInitialized()) {
                    for (Entity entity : this.getEntities()) {
                        if (((ImplEntity) entity).canBeSpawnedTo(player)) {
                            entity.spawnTo(player);
                        }
                    }
                }
            }).schedule();
        } catch (IOException exception) {
            this.getWorld().getServer().getLogger().error("Failed to serialize blocks", exception);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Set<Player> getViewers() {
        return new HashSet<>(this.spawnedTo);
    }

    public void despawnFrom(Player player) {
        if (this.spawnedTo.remove(player)) {
            for (Entity entity : this.getEntities()) {
                if (!entity.equals(player) && entity.hasSpawnedTo(player)) {
                    entity.despawnFrom(player);
                }
            }
        }
    }

    public void addChunkLoader() {
        this.activeChunkLoaders.getAndIncrement();
        this.resetExpiryTime();
    }

    public void removeChunkLoader() {
        this.activeChunkLoaders.getAndDecrement();
    }

    /**
     * Resets the expiry time for this chunk to request to be unloaded.
     */
    private void resetExpiryTime() {
        this.expiryTimer = this.world.getServer().getConfig().getChunkExpiryTime() * 20;
    }

    @Override
    public boolean canBeClosed() {
        return this.spawnedTo.size() == 0 && this.activeChunkLoaders.get() == 0;
    }

    @Override
    public void close() {
        this.close(true, true);
    }

    public void close(boolean async, boolean force) {
        this.getWorld().getChunkManager().unloadChunk(this.getX(), this.getZ(), async, force);
    }

    @Override
    public int hashCode() {
        return 7 + (37 * this.getX()) + (37 * this.getZ()) + (37 * this.getWorld().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplChunk) {
            ImplChunk otherChunk = (ImplChunk) obj;
            return (otherChunk.getX() == this.getX()) && (otherChunk.getZ() == this.getZ()) && (otherChunk.getWorld().equals(this.getWorld()));
        }
        return false;
    }


    public static class Builder {

        private BedrockChunk chunk;

        private ImplWorld world;
        private int x;
        private int z;


        public Builder setChunk(BedrockChunk chunk) {
            this.chunk = chunk;
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
            return new ImplChunk(this.world, this.x, this.z, this.chunk);
        }


    }

}
