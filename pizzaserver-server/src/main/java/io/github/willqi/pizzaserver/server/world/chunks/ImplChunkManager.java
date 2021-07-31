package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.ReadWriteKeyLock;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.server.world.ImplWorld;
import io.github.willqi.pizzaserver.server.world.chunks.processing.ChunkQueue;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.PlayerChunkRequest;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.UnloadChunkRequest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImplChunkManager implements ChunkManager {

    private final ImplWorld world;
    private final Map<Tuple<Integer, Integer>, Chunk> chunks = new ConcurrentHashMap<>();
    private final ReadWriteKeyLock<Tuple<Integer, Integer>> lock = new ReadWriteKeyLock<>();

    private final ChunkQueue chunkQueue = new ChunkQueue(this);


    public ImplChunkManager(ImplWorld world) {
        this.world = world;
    }

    public ImplWorld getWorld() {
        return this.world;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        Tuple<Integer, Integer> key = new Tuple<>(x, z);
        this.lock.readLock(key);
        try {
            return this.chunks.containsKey(key);
        } finally {
            this.lock.readUnlock(key);
        }
    }

    @Override
    public Chunk getChunk(int x, int z) {
        return this.getChunk(x, z, true);
    }

    @Override
    public Chunk getChunk(int x, int z, boolean loadFromProvider) {
        Tuple<Integer, Integer> key = new Tuple<>(x, z);
        this.lock.readLock(key);

        try {
            this.chunks.computeIfAbsent(key, v -> {
                if (!loadFromProvider) {
                    return null;
                }

                // Load chunk from provider
                Chunk chunk = null;
                try {
                    BedrockChunk internalChunk = this.getWorld().getProvider().getChunk(x, z);

                    chunk = new ImplChunk.Builder()
                            .setWorld(world)
                            .setX(internalChunk.getX())
                            .setZ(internalChunk.getZ())
                            .setSubChunks(internalChunk.getSubChunks())
                            .build();
                } catch (IOException exception) {
                    this.getWorld().getServer().getLogger().error(String.format("Failed to retrieve chunk (%s, %s) from provider", x, z), exception);
                }
                return chunk;
            });

            return this.chunks.getOrDefault(key, null);
        } finally {
            this.lock.readUnlock(key);
        }
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        Tuple<Integer, Integer> key = new Tuple<>(x, z);
        this.lock.writeLock(key);

        try {
            Chunk chunk = this.chunks.getOrDefault(key, null);
            if (Check.isNull(chunk) || !chunk.canBeClosed()) {
                return false;
            }

            this.chunks.remove(key);
            chunk.close();
            return true;
        } finally {
            this.lock.writeUnlock(key);
        }
    }

    @Override
    public void unloadChunkRequest(int x, int z) {
        this.chunkQueue.addRequest(new UnloadChunkRequest(x, z));
    }

    @Override
    public void sendPlayerChunk(Player player, int x, int z) {
        Tuple<Integer, Integer> key = new Tuple<>(x, z);
        this.lock.readLock(key);
        try {
            Chunk chunk = this.getChunk(x, z);
            chunk.sendTo(player);
        } finally {
            this.lock.readUnlock(key);
        }
    }

    @Override
    public void sendPlayerChunkRequest(Player player, int x, int z) {
        this.chunkQueue.addRequest(new PlayerChunkRequest(player, x, z));
    }

    @Override
    public void close() throws IOException {
        this.chunkQueue.close();
        // TODO: unload and save all chunks
    }

}
