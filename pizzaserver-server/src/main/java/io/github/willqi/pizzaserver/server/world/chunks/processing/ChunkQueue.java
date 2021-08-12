package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.ChunkRequest;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.PlayerChunkRequest;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.UnloadChunkRequest;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The ChunkQueue handles sending chunks and unloading chunks asynchronously.
 * Only a certain amount (defined in configuration) of requests can be made per player each tick.
 * In addition, the internal chunk processor has its own limit set in configuration as to the amount of requests that can be queued per player/for unload tasks.
 */
public class ChunkQueue implements Closeable {

    private final Map<ImplPlayer, Set<PlayerChunkRequest>> queuedSendRequests = new ConcurrentHashMap<>();
    private final Map<ImplPlayer, AtomicInteger> activeChunkSendRequests = new ConcurrentHashMap<>();

    private final Set<UnloadChunkRequest> queuedUnloadChunkRequests = ConcurrentHashMap.newKeySet();
    private final AtomicInteger activeUnloadChunkRequests = new AtomicInteger();

    private final ChunkManager chunkManager;
    private final ChunkProcessingThread processor;

    public ChunkQueue(ChunkManager manager) {
        this.chunkManager = manager;
        this.processor = new ChunkProcessingThread(manager, this);
    }

    /**
     * Add a request to the queue for this world
     * @param request The {@link ChunkRequest} to add to the queue
     */
    public void addRequest(ChunkRequest request) {
        if (request instanceof PlayerChunkRequest) {
            ImplPlayer player = ((PlayerChunkRequest)request).getPlayer();
            this.queuedSendRequests.computeIfAbsent(player, ignored -> ConcurrentHashMap.newKeySet());

            Set<PlayerChunkRequest> playerSendRequests = this.queuedSendRequests.getOrDefault(player, null);
            if (playerSendRequests != null) {
                playerSendRequests.add((PlayerChunkRequest)request);
            }
        } else {
            this.queuedUnloadChunkRequests.add((UnloadChunkRequest)request);
        }
    }

    // Ticked by the main thread
    public void tick() {
        // Send player chunks
        Iterator<ImplPlayer> playerQueueIterator = this.queuedSendRequests.keySet().iterator();
        while (playerQueueIterator.hasNext()) {
            ImplPlayer player = playerQueueIterator.next();
            Set<PlayerChunkRequest> playerSendRequests = this.queuedSendRequests.get(player);

            // Check if we should push player chunk requests to the processor
            if (
                    playerSendRequests.size() > 0 &&
                    player.isConnected() &&
                    player.getLocation().getWorld().equals(this.chunkManager.getWorld())
            ) {  // Send the amount of chunks we can send this for this player during this tick
                Iterator<PlayerChunkRequest> requestsInterator = playerSendRequests.iterator();

                while (requestsInterator.hasNext() && this.hasRoomToSendChunkToPlayer(player) && player.acknowledgeChunkSendRequest()) {
                    this.addedChunkToPlayerQueue(player);
                    this.processor.addRequest(requestsInterator.next());
                    requestsInterator.remove();
                }
            } else {    // Player is no longer connected/has no more chunk requests.
                playerQueueIterator.remove();
            }
        }

        // Unload some chunks if any exist
        if (this.queuedUnloadChunkRequests.size() > 0) {
            Iterator<UnloadChunkRequest> iterator = this.queuedUnloadChunkRequests.iterator();
            // Check how many chunks we can offload per tick
            int queueLeft = ((ImplServer)this.chunkManager.getWorld().getServer()).getConfig().getChunkRequestsPerTick();
            while (iterator.hasNext() && queueLeft > 0 && this.hasRoomToUnloadChunk()) {
                UnloadChunkRequest request = iterator.next();
                this.activeUnloadChunkRequests.getAndIncrement();
                this.processor.addRequest(request);
                iterator.remove();
            }
        }
    }

    /**
     * Checks if there is enough room in the chunk's current queue to send a chunk to a player
     * The player's queue cannot exceed the configured chunk tick count
     * @param player the player to send a chunk to
     * @return if there is room
     */
    private boolean hasRoomToSendChunkToPlayer(ImplPlayer player) {
        return !this.activeChunkSendRequests.containsKey(player) || this.activeChunkSendRequests.get(player).get() < player.getServer().getConfig().getChunkProcessingCap();
    }

    /**
     * Notes to the internal queue that a player has a pending chunk request
     * Players cannot exceed the configured chunk tick count
     * @param player the player to send a chunk to
     */
    private void addedChunkToPlayerQueue(ImplPlayer player) {
        this.activeChunkSendRequests.computeIfAbsent(player, ignored -> new AtomicInteger());
        AtomicInteger currentCount = this.activeChunkSendRequests.getOrDefault(player, null);
        if (currentCount != null) {
            currentCount.getAndIncrement();
        } else {
            this.addedChunkToPlayerQueue(player);
        }
    }

    private boolean hasRoomToUnloadChunk() {
        return this.activeUnloadChunkRequests.get() < ((ImplServer)ImplServer.getInstance()).getConfig().getChunkProcessingCap();
    }

    /**
     * Called by the {@link ChunkProcessingThread} to notify that chunk request has been completed
     * Removes a chunk from the internal queue count
     * @param request the request that was completed
     */
    void onRequestCompletion(ChunkRequest request) {
        if (request instanceof PlayerChunkRequest) {
            PlayerChunkRequest playerChunkRequest = (PlayerChunkRequest)request;
            AtomicInteger playerChunkCount = this.activeChunkSendRequests.getOrDefault(playerChunkRequest.getPlayer(), null);
            if (playerChunkCount != null) {
                playerChunkCount.getAndDecrement();

                if (playerChunkCount.get() <= 0) {  // player has no more chunks queued: clean up cache
                    this.activeChunkSendRequests.remove(playerChunkRequest.getPlayer());
                }
            }
        } else {
            this.activeUnloadChunkRequests.getAndDecrement();
        }
    }

    @Override
    public void close() {
        this.processor.interrupt();
    }

}
