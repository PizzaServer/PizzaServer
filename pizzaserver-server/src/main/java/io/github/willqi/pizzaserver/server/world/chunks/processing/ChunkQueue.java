package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.ChunkRequest;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The ChunkQueue is used to manage the amount of threads that are accessing the provider asynchronously.
 * A new Thread is created
 */
public class ChunkQueue implements Closeable {

    private final static int REQUESTS_PER_THREAD_THRESHOLD = 1000;


    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final AtomicInteger activeThreads = new AtomicInteger(0);   // How many threads are running
    private volatile boolean canSendThreadRequest = true;  // if the queue can request another Thread to be started

    private final ChunkManager chunkManager;

    public ChunkQueue(ChunkManager manager) {
        this.chunkManager = manager;
    }

    public void addRequest(ChunkRequest request) {
        this.requests.add(request);

        boolean shouldStartupChunkThread = this.canSendThreadRequest && ((this.activeThreads.get() == 0) ||
                (this.requests.size() / this.activeThreads.get() > REQUESTS_PER_THREAD_THRESHOLD));

        if (shouldStartupChunkThread) {
            this.canSendThreadRequest = false;
            this.threadPool.execute(new ChunkProcessingRunnable(this.chunkManager, this));
        }
    }

    LinkedBlockingQueue<ChunkRequest> getRequests() {
        return this.requests;
    }

    void notifyStartup() {
        this.activeThreads.getAndIncrement();
        this.canSendThreadRequest = true;
    }

    void notifyShutdown() {
        this.activeThreads.getAndDecrement();
    }

    @Override
    public void close() {
        this.threadPool.shutdownNow();
    }

}
