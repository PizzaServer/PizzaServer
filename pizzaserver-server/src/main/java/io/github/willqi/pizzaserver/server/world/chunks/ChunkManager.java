package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChunkManager implements APIChunkManager {

    private final World world;
    private final Map<Tuple<Integer, Integer>, APIChunk> chunks = new HashMap<>();


    public ChunkManager(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.chunks.containsKey(new Tuple<>(x, z));
    }

    @Override
    public APIChunk getChunk(int x, int z) {
        return this.chunks.get(new Tuple<>(x, z));
    }

    @Override
    public CompletableFuture<Void> unloadChunk(int x, int z) {
        if (!this.isChunkLoaded(x, z)) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<Void> unloadRequest = CompletableFuture.completedFuture(null);
        // TODO: send request to unload chunk and save it to disk
        Tuple<Integer, Integer> chunkKey = new Tuple<>(x, z);
        if (this.chunks.containsKey(chunkKey)) {
            this.chunks.get(chunkKey).close();
            this.chunks.remove(new Tuple<>(x, z));
        }
        return unloadRequest;
    }

    @Override
    public CompletableFuture<APIChunk> fetchChunk(int x, int z) {
        if (this.isChunkLoaded(x, z)) {
            return CompletableFuture.completedFuture(this.getChunk(x, z));
        }
        CompletableFuture<APIChunk> chunkRequest = this.getWorld().getWorldThread().requestChunk(x, z);
        chunkRequest.whenComplete((chunk, exception) -> {
           if (chunk != null) {
                this.chunks.put(new Tuple<>(x, z), chunk);
           }
        });
        return chunkRequest;
    }

    /**
     * Request a {@link Chunk} to be sent to a {@link Player} asynchronously.
     * @param player the {@link Player} who the chunk should be sent to
     * @param chunk the {@link Chunk} to send to the player
     */
    public void addChunkToPlayerQueue(Player player, Chunk chunk) {
        this.getWorld().getWorldThread().requestSendChunkToPlayer(player, chunk).thenRun(() -> chunk.sendEntitiesTo(player));
    }

}
