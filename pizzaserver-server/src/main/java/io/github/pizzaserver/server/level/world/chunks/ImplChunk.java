package io.github.pizzaserver.server.level.world.chunks;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.BlockEventPacket;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.block.impl.BlockAir;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.format.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BedrockSubChunk;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BlockLayer;
import io.github.pizzaserver.format.dimension.chunks.subchunk.BlockPaletteEntry;
import io.github.pizzaserver.format.utils.BedrockNetworkUtils;
import io.github.pizzaserver.format.utils.VarInts;
import io.github.pizzaserver.server.blockentity.handler.BlockEntityHandler;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.level.world.chunks.data.BlockUpdateEntry;
import io.github.pizzaserver.server.level.world.chunks.utils.ChunkUtils;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;
import io.github.pizzaserver.server.network.protocol.version.V503MinecraftVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;

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
    private volatile boolean chunkWasModified;

    private final ImplWorld world;
    private final int x;
    private final int z;

    private final List<BlockUpdateEntry> blockUpdates = new ArrayList<>();

    private int expiryTimer;
    private final AtomicInteger activeChunkLoaders = new AtomicInteger(0);

    // Entities in this chunk
    private final Set<Entity> entities = new HashSet<>();
    private final Map<Vector3i, BaseBlockEntity<? extends Block>> blockEntities = new HashMap<>();

    // The players who can see this chunk
    private final Set<Player> spawnedTo = ConcurrentHashMap.newKeySet();


    @SuppressWarnings("rawtypes")
    protected ImplChunk(ImplWorld world, int x, int z, BedrockChunk chunk) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.chunk = chunk;
        this.resetExpiryTime();

        for (NbtMap blockEntityNBT : new HashSet<>(chunk.getBlockEntities().values())) {
            BlockEntity<? extends Block> blockEntity = BlockEntityHandler.fromDiskNBT(this.getWorld(), blockEntityNBT);
            this.addBlockEntity(blockEntity);
        }
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
    public int getBiomeAt(int x, int y, int z) {
        Check.withinBoundsInclusive(x, 0, 15, "x");
        Check.withinBoundsInclusive(y, -64, 320, "y");
        Check.withinBoundsInclusive(z, 0, 15, "z");

        int subChunkY = (int) Math.floor(y / 16d);
        synchronized (this.chunk) {
            return this.chunk.getBiomeMap().getSubChunk(subChunkY).getBiomeAt(x & 15, y & 15, z & 15);
        }
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

    public void addBlockEntity(BlockEntity<? extends Block> blockEntity) {
        Vector3i blockCoordinates = Vector3i.from(blockEntity.getLocation().getX() & 15, blockEntity.getLocation().getY(), blockEntity.getLocation().getZ() & 15);

        synchronized (this.chunk) {
            this.chunk.addBlockEntity(BlockEntityHandler.toDiskNBT(blockEntity));
            this.chunkWasModified = true;

            synchronized (this.blockEntities) {
                this.blockEntities.put(blockCoordinates, (BaseBlockEntity<? extends Block>) blockEntity);
            }
        }
    }

    public void removeBlockEntity(BlockEntity<? extends Block> blockEntity) {
        Vector3i blockCoordinates = Vector3i.from(blockEntity.getLocation().getX() & 15, blockEntity.getLocation().getY(), blockEntity.getLocation().getZ() & 15);

        synchronized (this.chunk) {
            this.chunk.removeBlockEntity(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
            this.chunkWasModified = true;

            synchronized (this.blockEntities) {
                this.blockEntities.remove(blockCoordinates);
            }
        }
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        int chunkBlockY;
        synchronized (this.chunk) {
            chunkBlockY = Math.max(0, this.chunk.getHeightMap().getHighestBlockAt(x & 15, z & 15) - 1);
        }
        return this.getBlock(x, chunkBlockY, z);
    }

    @Override
    public Block getBlock(Vector3i blockCoordinates, int layer) {
        return this.getBlock(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    @Override
    public Block getBlock(int x, int y, int z, int layer) {
        int subChunkIndex = (int) Math.floor(y / 16d);
        int chunkBlockX = x & 15;
        int chunkBlockY = y & 15;
        int chunkBlockZ = z & 15;
        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + chunkBlockX, y, this.getZ() * 16 + chunkBlockZ);

        // Check if block is out of bounds.
        if (y < this.getWorld().getDimension().getMinHeight() || y > this.getWorld().getDimension().getMaxHeight()) {
            Block airBlock = new BlockAir();
            airBlock.setLocation(this.getWorld(), blockCoordinates, layer);
            return airBlock;
        }

        Lock readLock = this.lock.readLock();
        readLock.lock();

        try {
            BlockPaletteEntry paletteEntry;
            synchronized (this.chunk) {
                BedrockSubChunk subChunk = this.getSubChunk(subChunkIndex);
                if (subChunk.getLayers().size() <= layer) {
                    // layer does not exist: return air block
                    Block block = BlockRegistry.getInstance().getBlock(BlockID.AIR);
                    block.setLocation(this.getWorld(), blockCoordinates, layer);
                    return block;
                }

                paletteEntry = subChunk.getLayer(layer).getBlockEntryAt(chunkBlockX, chunkBlockY, chunkBlockZ);
            }
            Block block;
            if (BlockRegistry.getInstance().hasBlock(paletteEntry.getId())) {
                // Block id is registered
                block = BlockRegistry.getInstance().getBlock(paletteEntry.getId());
                block.setBlockState(paletteEntry.getState());
            } else {
                // The block id is not registered
                this.getWorld().getServer().getLogger().warn("Could not find block type for id " + paletteEntry.getId() + ". Substituting with air");
                block = new BlockAir();
            }
            block.setLocation(this.getWorld(), blockCoordinates, layer);

            return block;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<BlockEntity<? extends Block>> getBlockEntity(int x, int y, int z) {
        Vector3i blockCoordinate = Vector3i.from(x & 15, y, z & 15);
        synchronized (this.blockEntities) {
            return Optional.ofNullable(this.blockEntities.getOrDefault(blockCoordinate, null));
        }
    }

    @Override
    public void setBlock(Block block, int x, int y, int z, int layer) {
        if (y < this.getWorld().getDimension().getMinHeight() || y > this.getWorld().getDimension().getMaxHeight()) {
            return;
        }

        int subChunkIndex = (int) Math.floor(y / 16d);
        int chunkBlockX = x & 15;
        int subChunkBlockY = y & 15;
        int chunkBlockZ = z & 15;

        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + chunkBlockX, y, this.getZ() * 16 + chunkBlockZ);

        block.setLocation(this.getWorld(), blockCoordinates, layer);

        Lock writeLock = this.lock.writeLock();
        writeLock.lock();

        // Remove old block entity at this position
        // only if the new block does not support this block entity type
        boolean tryAddingNewBlockEntity = true;
        Optional<BlockEntity<? extends Block>> oldBlockEntity = this.getBlockEntity(x, y, z);
        if (oldBlockEntity.isPresent()) {
            boolean oldBlockEntityCanStillBeUsed = ((BaseBlockEntity<? extends Block>) oldBlockEntity.get())
                    .getBlockIds().contains(block.getBlockId());

            if (oldBlockEntityCanStillBeUsed) {
                tryAddingNewBlockEntity = false;
            } else {
                this.removeBlockEntity(oldBlockEntity.get());
            }
        }

        // Add block entity if one exists for this block
        if (tryAddingNewBlockEntity) {
            BlockEntityHandler.create(block).ifPresent(this::addBlockEntity);
        }

        try {
            // Update internal sub chunk
            synchronized (this.chunk) {
                BedrockSubChunk subChunk = this.getSubChunk(subChunkIndex);

                BlockLayer mainBlockLayer = subChunk.getLayer(layer);
                BlockPaletteEntry entry = new BlockPaletteEntry(block.getBlockId(), ServerProtocol.LATEST_BLOCK_STATES_VERSION, block.getNBTState());
                mainBlockLayer.setBlockEntryAt(chunkBlockX, subChunkBlockY, chunkBlockZ, entry);

                int highestBlockY = Math.max(0, this.chunk.getHeightMap().getHighestBlockAt(chunkBlockX, chunkBlockZ) - 1);
                if (y >= highestBlockY) {
                    int newHighestBlockY = y;
                    while (this.getBlock(chunkBlockX, newHighestBlockY, chunkBlockZ).isAir() && newHighestBlockY >= -64) {
                        newHighestBlockY--;
                    }
                    this.chunk.getHeightMap().setHighestBlockAt(chunkBlockX, chunkBlockZ, newHighestBlockY + 1);
                }

                this.chunkWasModified = true;
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

        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.up(), 1);
        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.down(), 1);
        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.north(), 1);
        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.south(), 1);
        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.west(), 1);
        this.getWorld().requestBlockUpdate(BlockUpdateType.NEIGHBOUR, blockCoordinates.east(), 1);
    }

    @Override
    public boolean requestBlockUpdate(BlockUpdateType type, int x, int y, int z, int ticks) {
        Vector3i blockCoordinates = Vector3i.from(x & 15, y, z & 15);
        BlockUpdateEntry entry = new BlockUpdateEntry(type, blockCoordinates, ticks);
        this.blockUpdates.remove(entry);
        return this.blockUpdates.add(entry);
    }

    @Override
    public void addBlockEvent(int x, int y, int z, int type, int data) {
        for (Player viewer : this.getViewers()) {
            this.addBlockEvent(viewer, x, y, z, type, data);
        }
    }

    @Override
    public void addBlockEvent(Player player, int x, int y, int z, int type, int data) {
        Vector3i blockCoordinates = Vector3i.from(this.getX() * 16 + (x & 15), y, this.getZ() * 16 + (z & 15));
        BlockEventPacket blockEventPacket = new BlockEventPacket();
        blockEventPacket.setBlockPosition(blockCoordinates);
        blockEventPacket.setEventType(type);
        blockEventPacket.setEventData(data);

        player.sendPacket(blockEventPacket);
    }

    private void doBlockUpdate(BlockUpdateType type, int x, int y, int z) {
        int subChunkIndex = (int) Math.floor(y / 16d);
        int chunkBlockX = x & 15;
        int chunkBlockZ = z & 15;

        int layers;
        synchronized (this.chunk) {
            layers = Math.max(this.getSubChunk(subChunkIndex).getLayers().size(), 1);
        }

        for (int layer = 0; layer < layers; layer++) {
            Block block = this.getBlock(chunkBlockX, y, chunkBlockZ, layer);
            block.getBehavior().onUpdate(type, block);
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
        int subChunkIndex = (int) Math.floor(y / 16d);

        // Ensure that at least the foremost layer is sent to the client
        int layers;
        synchronized (this.chunk) {
            layers = Math.max(this.getSubChunk(subChunkIndex).getLayers().size(), 1);
        }

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
        updateBlockPacket.setRuntimeId(((BaseMinecraftVersion) player.getVersion()).getBlockRuntimeId(block.getBlockId(), block.getNBTState()));
        updateBlockPacket.setBlockPosition(Vector3i.from(chunkBlockX + this.getX() * 16, y, chunkBlockZ + this.getZ() * 16));
        updateBlockPacket.setDataLayer(layer);
        updateBlockPacket.getFlags().add(UpdateBlockPacket.Flag.NETWORK);
        player.sendPacket(updateBlockPacket);

        this.getWorld().getBlockEntity(block.getLocation().toVector3i()).ifPresent(blockEntity ->
                this.sendBlockEntityData(player, blockEntity));
    }

    protected void sendBlockEntityData(Player player, BlockEntity<? extends Block> blockEntity) {
        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition(blockEntity.getLocation().toVector3i());
        blockEntityDataPacket.setData(BlockEntityHandler.toNetworkNBT(blockEntity));
        player.sendPacket(blockEntityDataPacket);
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

            synchronized (this.blockEntities) {
                for (BaseBlockEntity<? extends Block> blockEntity : this.blockEntities.values()) {
                    blockEntity.tick();
                    if (blockEntity.requestedUpdate()) {
                        for (Player player : this.getViewers()) {
                            this.sendBlockEntityData(player, blockEntity);
                        }
                    }
                }
            }

            List<BlockUpdateEntry> currentTickBlockUpdates = new ArrayList<>(this.blockUpdates);
            this.blockUpdates.clear();
            for (BlockUpdateEntry blockUpdateEntry : currentTickBlockUpdates) {
                if (blockUpdateEntry.getTicksLeft() > 0) {
                    blockUpdateEntry.tick();
                    this.blockUpdates.add(blockUpdateEntry);
                } else {
                    Vector3i blockCoordinates = blockUpdateEntry.getBlockCoordinates();
                    this.doBlockUpdate(blockUpdateEntry.getType(), blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
                }
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

        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            int subChunkCount;

            synchronized (this.chunk) {
                subChunkCount = ChunkUtils.getSubChunkCount(this.chunk);

                for (int i = -4; i < subChunkCount - 4; i++) {
                    BedrockNetworkUtils.serializeSubChunk(buffer, this.chunk.getSubChunk(i), false, (BaseMinecraftVersion) player.getVersion());
                }

                // Write biomes
                int biomeSubChunksToWrite = this.getWorld().getDimension()
                        .getHeight() / 16;
                if (player.getVersion().getProtocol() < V503MinecraftVersion.PROTOCOL) {
                    // For some reason 1.18.12 and below have an extra biome sub chunk that can be written.
                    biomeSubChunksToWrite++;
                }

                BedrockNetworkUtils.serialize3DBiomeMap(buffer, this.chunk.getBiomeMap(), biomeSubChunksToWrite);

                buffer.writeByte(0);    // border blocks
                VarInts.writeUnsignedInt(buffer, 0);    // extra data

                // Write block entities if any exist
                if (!this.chunk.getBlockEntities().isEmpty()) {
                    try (NBTOutputStream outputStream = NbtUtils.createNetworkWriter(new ByteBufOutputStream(buffer))) {
                        for (NbtMap diskBlockEntityNBT : this.chunk.getBlockEntities().values()) {
                            try {
                                NbtMap networkBlockEntityNBT = ((BaseMinecraftVersion) player.getVersion()).getNetworkBlockEntityNBT(diskBlockEntityNBT);
                                outputStream.writeTag(networkBlockEntityNBT);
                            } catch (NullPointerException exception) {
                                throw new IOException("Failed to send chunk due to unhandled block entity found: " + diskBlockEntityNBT);
                            }
                        }
                    }
                }
            }

            byte[] chunkData = new byte[buffer.readableBytes()];
            buffer.readBytes(chunkData);

            // Ran on the main thread in order because player.getLocation() is not thread safe
            final int packetSubChunkCount = subChunkCount;
            this.getWorld().getServer().getScheduler().prepareTask(() -> {
                if (player.isConnected() && player.getLocation().getWorld().equals(this.getWorld())) {
                    LevelChunkPacket chunkPacket = new LevelChunkPacket();
                    chunkPacket.setChunkX(this.getX());
                    chunkPacket.setChunkZ(this.getZ());
                    chunkPacket.setSubChunksLength(packetSubChunkCount);
                    chunkPacket.setData(chunkData);
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
            buffer.release();
            readLock.unlock();
        }
    }

    private BedrockSubChunk getSubChunk(int subChunkY) {
        try {
            return this.chunk.getSubChunk(subChunkY);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to fetch sub chunk.");
        }
    }

    public void save() throws IOException {
        synchronized (this.chunk) {
            if (this.chunkWasModified) {
                synchronized (this.blockEntities) {
                    for (BlockEntity<? extends Block> blockEntity : this.blockEntities.values()) {
                        this.chunk.addBlockEntity(BlockEntityHandler.toDiskNBT(blockEntity));
                    }
                }

                this.world.getLevel().getProvider().getDimension(this.chunk.getDimension())
                        .saveChunk(this.chunk);
            }
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
        if (obj instanceof ImplChunk otherChunk) {
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
            Check.notNull(this.world, "world");
            Check.notNull(this.chunk, "chunk");
            return new ImplChunk(this.world, this.x, this.z, this.chunk);
        }


    }

}
