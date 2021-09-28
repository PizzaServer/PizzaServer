package io.github.willqi.pizzaserver.server.level.world.chunks;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.level.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.ReadWriteKeyLock;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.format.api.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;
import io.github.willqi.pizzaserver.server.level.processing.requests.PlayerChunkRequest;
import io.github.willqi.pizzaserver.server.level.processing.requests.UnloadChunkRequest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImplChunkManager implements ChunkManager {

    private final ImplWorld world;
    private final Map<Tuple<Integer, Integer>, ImplChunk> chunks = new ConcurrentHashMap<>();
    private final ReadWriteKeyLock<Tuple<Integer, Integer>> lock = new ReadWriteKeyLock<>();


    public ImplChunkManager(ImplWorld world) {
        this.world = world;
    }

    /**
     * Tick all chunks and the chunk queue.
     */
    public void tick() {
        for (ImplChunk chunk : this.chunks.values()) {
            chunk.tick();
        }
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
    public ImplChunk getChunk(int x, int z) {
        return this.getChunk(x, z, true);
    }

    @Override
    public ImplChunk getChunk(int x, int z, boolean loadFromProvider) {
        Tuple<Integer, Integer> key = new Tuple<>(x, z);
        this.lock.readLock(key);

        try {
            this.chunks.computeIfAbsent(key, v -> {
                if (!loadFromProvider) {
                    return null;
                }

                // Load chunk from provider
                ImplChunk chunk = null;
                try {
                    BedrockChunk internalChunk = this.getWorld().getLevel().getProvider().getChunk(x, z, this.getWorld().getDimension());

                    chunk = new ImplChunk.Builder()
                            .setWorld(this.world)
                            .setX(internalChunk.getX())
                            .setZ(internalChunk.getZ())
                            .setChunk(internalChunk)
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
    public void unloadChunk(int x, int z) {
        this.unloadChunk(x, z, false, false);
    }

    @Override
    public void unloadChunk(int x, int z, boolean async, boolean force) {
        if (async) {
            this.getWorld().getLevel()
                    .getLevelManager()
                    .getProcessorManager()
                    .addRequest(new UnloadChunkRequest(this.getWorld(), x, z));
        } else {
            Tuple<Integer, Integer> key = new Tuple<>(x, z);
            this.lock.writeLock(key);

            try {
                Chunk chunk = this.chunks.getOrDefault(key, null);
                if (Check.isNull(chunk) || (!chunk.canBeClosed() && !force)) {
                    return;
                }

                this.chunks.remove(key);
                chunk.close();
            } finally {
                this.lock.writeUnlock(key);
            }
        }
    }

    @Override
    public void sendPlayerChunk(Player player, int x, int z) {
        this.sendPlayerChunk(player, x, z, false);
    }

    @Override
    public void sendPlayerChunk(Player player, int x, int z, boolean async) {
        if (async) {
            this.getWorld()
                    .getLevel()
                    .getLevelManager()
                    .getProcessorManager()
                    .addRequest(new PlayerChunkRequest((ImplPlayer) player, x, z));
        } else {
            Tuple<Integer, Integer> key = new Tuple<>(x, z);
            this.lock.readLock(key);
            try {
                ImplChunk chunk = this.getChunk(x, z);
                chunk.spawnTo(player);
            } finally {
                this.lock.readUnlock(key);
            }
        }
    }

    @Override
    public void close() throws IOException {
        for (Chunk chunk : this.chunks.values()) {
            this.unloadChunk(chunk.getX(), chunk.getZ(), false, true);
        }
    }

    @Override
    public ImplWorld getWorld() {
        return this.world;
    }

}
