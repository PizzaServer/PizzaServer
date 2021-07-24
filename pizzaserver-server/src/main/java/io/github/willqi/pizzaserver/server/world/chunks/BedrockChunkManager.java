package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.server.player.BedrockPlayer;
import io.github.willqi.pizzaserver.server.world.BedrockWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BedrockChunkManager implements ChunkManager {

    private final BedrockWorld world;
    private final Map<Tuple<Integer, Integer>, Chunk> chunks = new HashMap<>();


    public BedrockChunkManager(BedrockWorld world) {
        this.world = world;
    }

    public BedrockWorld getWorld() {
        return this.world;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.chunks.containsKey(new Tuple<>(x, z));
    }

    @Override
    public Chunk getChunk(int x, int z) {
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
    public CompletableFuture<Chunk> fetchChunk(int x, int z) {
        if (this.isChunkLoaded(x, z)) {
            return CompletableFuture.completedFuture(this.getChunk(x, z));
        }
        CompletableFuture<Chunk> chunkRequest = this.getWorld().getWorldThread().requestChunk(x, z);
        chunkRequest.whenComplete((chunk, exception) -> {
           if (chunk != null) {
                this.chunks.put(new Tuple<>(x, z), chunk);
           }
        });
        return chunkRequest;
    }

    /**
     * Request a {@link BedrockChunk} to be sent to a {@link BedrockPlayer} asynchronously.
     * @param player the {@link BedrockPlayer} who the chunk should be sent to
     * @param chunk the {@link BedrockChunk} to send to the player
     */
    public void addChunkToPlayerQueue(BedrockPlayer player, BedrockChunk chunk) {
        this.getWorld().getWorldThread().requestSendChunkToPlayer(player, chunk).thenRun(() -> chunk.sendEntitiesTo(player));
    }

}
