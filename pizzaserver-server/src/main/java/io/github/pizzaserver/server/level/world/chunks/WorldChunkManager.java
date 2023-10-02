package io.github.pizzaserver.server.level.world.chunks;

import org.cloudburstmc.math.vector.Vector2i;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.level.world.chunks.ChunkManager;
import io.github.pizzaserver.api.level.world.chunks.loader.ChunkLoader;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.commons.utils.ReadWriteKeyLock;
import io.github.pizzaserver.commons.utils.Tuple;
import io.github.pizzaserver.format.dimension.chunks.BedrockChunk;
import io.github.pizzaserver.server.level.processing.requests.PlayerChunkRequest;
import io.github.pizzaserver.server.level.processing.requests.UnloadChunkRequest;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WorldChunkManager implements ChunkManager {

    private final ImplWorld world;
    private final Map<Tuple<Integer, Integer>, ImplChunk> chunks = new ConcurrentHashMap<>();
    private final ReadWriteKeyLock<Tuple<Integer, Integer>> lock = new ReadWriteKeyLock<>();

    private final Set<ChunkLoader> chunkLoaders = new HashSet<>();


    public WorldChunkManager(ImplWorld world) {
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
                    BedrockChunk internalChunk = this.world.getLevel()
                            .getProvider()
                            .getDimension(this.world.getDimension().ordinal())
                            .getChunk(x, z);

                    chunk = new ImplChunk.Builder()
                            .setWorld(this.world)
                            .setX(internalChunk.getX())
                            .setZ(internalChunk.getZ())
                            .setChunk(internalChunk)
                            .build();
                } catch (IOException exception) {
                    this.world.getServer().getLogger().error(String.format("Failed to retrieve chunk (%s, %s) from provider", x, z), exception);
                }
                return chunk;
            });

            return this.chunks.getOrDefault(key, null);
        } finally {
            this.lock.readUnlock(key);
        }
    }

    public void unloadChunk(int x, int z) {
        this.unloadChunk(x, z, false, false);
    }

    public void unloadChunk(int x, int z, boolean async, boolean force) {
        if (async) {
            this.world.getLevel()
                    .getLevelManager()
                    .getProcessorManager()
                    .addRequest(new UnloadChunkRequest(this.world, x, z, force));
        } else {
            Tuple<Integer, Integer> key = new Tuple<>(x, z);
            this.lock.writeLock(key);

            try {
                ImplChunk chunk = this.chunks.getOrDefault(key, null);
                if (Check.isNull(chunk) || (!chunk.canBeClosed() && !force)) {
                    return;
                }

                if (Server.getInstance().getConfig().isSavingEnabled()) {
                    try {
                        chunk.save();
                    } catch (IOException exception) {
                        Server.getInstance().getLogger().error("Failed to save chunK", exception);
                    }
                }

                this.chunks.remove(key);
            } finally {
                this.lock.writeUnlock(key);
            }
        }
    }

    @Override
    public void sendChunk(Player player, int x, int z) {
        this.sendChunk(player, x, z, false);
    }

    @Override
    public void sendChunk(Player player, int x, int z, boolean async) {
        if (async) {
            this.world.getLevel()
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
    public boolean addChunkLoader(ChunkLoader chunkLoader) {
        if (this.chunkLoaders.add(chunkLoader)) {
            for (Vector2i coordinate : chunkLoader.getCoordinates()) {
                this.getChunk(coordinate.getX(), coordinate.getY()).addChunkLoader();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeChunkLoader(ChunkLoader chunkLoader) {
        if (this.chunkLoaders.remove(chunkLoader)) {
            for (Vector2i coordinate : chunkLoader.getCoordinates()) {
                Chunk chunk = this.getChunk(coordinate.getX(), coordinate.getY(), false);
                if (chunk != null) {
                    this.getChunk(coordinate.getX(), coordinate.getY()).removeChunkLoader();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        for (ImplChunk chunk : this.chunks.values()) {
            chunk.close(false, true);
        }
    }

}
