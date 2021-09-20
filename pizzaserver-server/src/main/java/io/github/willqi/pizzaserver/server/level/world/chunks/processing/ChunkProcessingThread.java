package io.github.willqi.pizzaserver.server.level.world.chunks.processing;

import io.github.willqi.pizzaserver.api.level.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.level.world.chunks.processing.requests.ChunkRequest;
import io.github.willqi.pizzaserver.server.level.world.chunks.processing.requests.PlayerChunkRequest;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles unloading and sending chunks to players on another Thread.
 */
public class ChunkProcessingThread extends Thread {

    private final ChunkManager chunkManager;
    private final ChunkQueue queue;

    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();


    public ChunkProcessingThread(ChunkManager manager, ChunkQueue queue) {
        this.chunkManager = manager;
        this.queue = queue;
        this.start();
    }

    /**
     * Add a request to the queue.
     * @param request chunk request
     */
    public void addRequest(ChunkRequest request) {
        this.requests.add(request);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ChunkRequest request;
            try {
                request = this.requests.take();
            } catch (InterruptedException exception) {
                break;  // Thread interrupted
            }

            if (request instanceof PlayerChunkRequest) {
                // send chunk request
                PlayerChunkRequest playerChunkRequest = (PlayerChunkRequest) request;
                this.chunkManager.sendPlayerChunk(playerChunkRequest.getPlayer(), request.getX(), request.getZ());
            } else {
                // unload request
                this.chunkManager.unloadChunk(request.getX(), request.getZ(), false, false);
            }
            this.queue.onRequestCompletion(request);
        }
    }

}
