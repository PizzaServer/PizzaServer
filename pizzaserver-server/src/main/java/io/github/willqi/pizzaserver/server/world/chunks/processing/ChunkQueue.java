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
 * The ChunkQueue is used to manage the amount of threads that are accessing the provider asynchronously.
 * A new Thread is created everytime the requests exceeds the threshold.
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

    public void addRequest(ChunkRequest request) {
        if (request instanceof PlayerChunkRequest) {
            ImplPlayer player = ((PlayerChunkRequest)request).getPlayer();
            this.queuedSendRequests.computeIfAbsent(player, ignored -> ConcurrentHashMap.newKeySet());

            Set<PlayerChunkRequest> requests = this.queuedSendRequests.getOrDefault(player, null);
            if (requests != null) {
                requests.add((PlayerChunkRequest)request);
            }

        } else {
            this.queuedUnloadChunkRequests.add((UnloadChunkRequest)request);
        }
    }

    // Ticked by the main thread
    public void tick() {
        ((ImplServer)this.chunkManager.getWorld().getServer()).getConfig().getChunkRequestsPerTick();

        // Send player chunks
        Iterator<ImplPlayer> playerQueueIterator = this.queuedSendRequests.keySet().iterator();
        while (playerQueueIterator.hasNext()) {
            ImplPlayer player = playerQueueIterator.next();
            Set<PlayerChunkRequest> requests = this.queuedSendRequests.get(player);

            if (requests.size() > 0 && player.isConnected()) {
                Iterator<PlayerChunkRequest> requestsInterator = requests.iterator();
                while (requestsInterator.hasNext() && player.acknowledgeChunkSendRequest()) {
                    this.processor.addRequest(requestsInterator.next());
                    requestsInterator.remove();
                }
            } else {
                playerQueueIterator.remove();
            }
        }

        // Unload some chunks if any exist
        if (this.queuedUnloadChunkRequests.size() > 0) {
            Iterator<UnloadChunkRequest> iterator = this.queuedUnloadChunkRequests.iterator();
            // How many chunks can we unload per tick?
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
