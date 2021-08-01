package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.ChunkRequest;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.PlayerChunkRequest;

import java.util.concurrent.LinkedBlockingQueue;

public class ChunkProcessingThread extends Thread {

    private final ChunkManager chunkManager;

    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();


    public ChunkProcessingThread(ChunkManager manager) {
        this.chunkManager = manager;
        this.start();
    }

    /**
     * Add a request to the queue
     * @param request
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
                this.chunkManager.sendPlayerChunk(((PlayerChunkRequest)request).getPlayer(), request.getX(), request.getZ());
            } else {
                // unload request
                this.chunkManager.unloadChunk(request.getX(), request.getZ());
            }
        }
    }

}
