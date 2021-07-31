package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.chunks.processing.requests.ChunkRequest;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The ChunkQueue is used to manage the amount of threads that are accessing the provider asynchronously.
 * A new Thread is created everytime the requests exceeds the threshold.
 */
public class ChunkQueue implements Closeable {

    private final static int REQUESTS_PER_THREAD_THRESHOLD = 1000;
    private final static int THREAD_CLOSING_INTERVAL = 30000;


    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final Set<Thread> activeThreads = ConcurrentHashMap.newKeySet();
    private volatile boolean canSendThreadRequest = true;  // if the queue can request another Thread to be started

    // Used to close unnecessary Threads when the queue is less busy
    private final AtomicInteger requestsProcessedInInterval = new AtomicInteger();   // how many threads were processed in the last interval?
    private volatile long nextCheck = 0;                                             // ms of when to check the processed count again

    private final ChunkManager chunkManager;

    public ChunkQueue(ChunkManager manager) {
        this.chunkManager = manager;
    }

    public void addRequest(ChunkRequest request) {
        this.requests.add(request);
        this.requestsProcessedInInterval.getAndIncrement();

        boolean shouldStartupChunkThread = this.canSendThreadRequest && ((this.activeThreads.size() == 0) ||
                (this.requests.size() / this.activeThreads.size() > REQUESTS_PER_THREAD_THRESHOLD));

        if (shouldStartupChunkThread) {
            this.canSendThreadRequest = false;
            this.threadPool.execute(new ChunkProcessingRunnable(this.chunkManager, this));
        } else {
            this.tryClosingExcessThreads();
        }
    }

    private void tryClosingExcessThreads() {
        synchronized (this) {
            if (System.currentTimeMillis() > this.nextCheck) {
                this.nextCheck = System.currentTimeMillis() + THREAD_CLOSING_INTERVAL;

                int recommendedThreadCount = (int)Math.ceil((double)this.requestsProcessedInInterval.get() / REQUESTS_PER_THREAD_THRESHOLD);
                this.requestsProcessedInInterval.set(0);

                Iterator<Thread> threadIterator = this.activeThreads.iterator();
                while (recommendedThreadCount < this.activeThreads.size() && threadIterator.hasNext()) {
                    Thread thread = threadIterator.next();
                    thread.interrupt();
                    threadIterator.remove();
                }
            }
        }
    }



    LinkedBlockingQueue<ChunkRequest> getRequests() {
        return this.requests;
    }

    void notifyStartup() {
        this.activeThreads.add(Thread.currentThread());
        this.canSendThreadRequest = true;
    }

    void notifyShutdown() {
        this.activeThreads.remove(Thread.currentThread());
    }

    @Override
    public void close() {
        this.threadPool.shutdownNow();
    }

}
