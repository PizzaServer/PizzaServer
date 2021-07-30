package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.ChunkRequest;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.PlayerChunkRequest;

import java.util.concurrent.TimeUnit;

public class ChunkProcessingRunnable implements Runnable {

    private final ChunkManager chunkManager;
    private final ChunkQueue queue;


    public ChunkProcessingRunnable(ChunkManager manager, ChunkQueue queue) {
        this.chunkManager = manager;
        this.queue = queue;
    }

    @Override
    public void run() {
        this.queue.notifyStartup();
        while (!Thread.currentThread().isInterrupted()) {
            ChunkRequest request;
            try {
                request = this.queue.getRequests().poll(30, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                break;  // Thread interrupted
            }
            if (request == null) {
                break; // This thread can now go back to the pool as we do not need this many threads processing chunks
            }

            if (request instanceof PlayerChunkRequest) {
                // send chunk request
                this.chunkManager.sendPlayerChunk(((PlayerChunkRequest)request).getPlayer(), request.getX(), request.getZ());
            } else {
                // unload request
                this.chunkManager.unloadChunk(request.getX(), request.getZ());
            }
        }
        this.queue.notifyShutdown();
    }

}
