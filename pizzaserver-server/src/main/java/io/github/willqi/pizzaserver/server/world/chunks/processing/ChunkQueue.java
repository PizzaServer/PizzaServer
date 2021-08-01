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

/**
 * The ChunkQueue handles sending chunks and unloading chunks asynchronously.
 * Only a certain amount (defined in configuration) of requests can be made per player each tick.
 * This amount is also applied to the amount of chunks that can be unloaded each tick.
 */
public class ChunkQueue implements Closeable {

    private final Map<ImplPlayer, Set<PlayerChunkRequest>> queuedSendRequests = new ConcurrentHashMap<>();
    private final Set<UnloadChunkRequest> queuedUnloadChunkRequests = ConcurrentHashMap.newKeySet();

    private final ChunkManager chunkManager;
    private final ChunkProcessingThread processor;

    public ChunkQueue(ChunkManager manager) {
        this.chunkManager = manager;
        this.processor = new ChunkProcessingThread(manager);
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
                while (requestsInterator.hasNext() && player.acknowledgeChunkSendRequest()) {
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
            while (iterator.hasNext() && queueLeft > 0) {
                UnloadChunkRequest request = iterator.next();
                this.processor.addRequest(request);
                iterator.remove();
            }
        }
    }

    @Override
    public void close() {
        this.processor.interrupt();
    }

}
