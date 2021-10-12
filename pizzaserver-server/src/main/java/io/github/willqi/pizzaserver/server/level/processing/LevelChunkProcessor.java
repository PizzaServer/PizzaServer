package io.github.willqi.pizzaserver.server.level.processing;

import io.github.willqi.pizzaserver.server.level.processing.requests.ChunkRequest;
import io.github.willqi.pizzaserver.server.level.processing.requests.PlayerChunkRequest;
import io.github.willqi.pizzaserver.server.level.processing.requests.UnloadChunkRequest;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsible for processing chunk requests asynchronously.
 * LevelChunkProcessors can only process x amount of chunks per tick to prevent a thread from hogging
 * all the resources and blocking other threads.
 */
public class LevelChunkProcessor implements Runnable {

    private final LevelChunkProcessorManager levelChunkProcessorManager;
    private final AtomicInteger allowedRequests = new AtomicInteger(0);

    public LevelChunkProcessor(LevelChunkProcessorManager levelChunkProcessorManager) {
        this.levelChunkProcessorManager = levelChunkProcessorManager;
        this.reset();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (this.allowedRequests.get() <= 0) {
                    // Wait until the next tick so that we can accept more requests
                    synchronized (this) {
                        this.wait();
                    }
                }
                this.allowedRequests.getAndDecrement();

                ChunkRequest request = this.levelChunkProcessorManager.takeRequest();
                if (request instanceof PlayerChunkRequest) {
                    // send chunk request
                    PlayerChunkRequest playerChunkRequest = (PlayerChunkRequest) request;
                    request.getWorld().sendChunk(playerChunkRequest.getPlayer(), request.getX(), request.getZ(), false);
                } else {
                    // unload request
                    ((ImplWorld) request.getWorld()).getChunkManager().unloadChunk(request.getX(), request.getZ(), false, ((UnloadChunkRequest) request).isForced());
                }
            } catch (InterruptedException ignored) {
                return;
            }


        }
    }

    /**
     * Resets the amount of chunk requests this processor can take in this tick.
     */
    public void reset() {
        this.allowedRequests.set(this.levelChunkProcessorManager.getLevelManager().getServer().getConfig().getMaxChunkProcessingCountPerTick());

        // Notify thread if it is asleep so that it can take in more requests
        synchronized (this) {
            this.notify();
        }
    }

}
