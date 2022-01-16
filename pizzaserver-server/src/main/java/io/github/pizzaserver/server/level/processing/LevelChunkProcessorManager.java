package io.github.pizzaserver.server.level.processing;

import io.github.pizzaserver.server.level.ImplLevelManager;
import io.github.pizzaserver.server.level.processing.requests.ChunkRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The LevelChunkProcessorManager starts up x threads which have the responsibility of handling chunk requests.
 */
public class LevelChunkProcessorManager {

    private final ImplLevelManager levelManager;

    private final BlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();

    private final ExecutorService processorsThreadPool;
    private final AtomicInteger processorThreadCount = new AtomicInteger(0);

    private final Set<LevelChunkProcessor> processors = new HashSet<>();


    public LevelChunkProcessorManager(ImplLevelManager levelManager) {
        this.levelManager = levelManager;

        // Create LevelChunkProcessor thread pool
        this.processorsThreadPool = Executors.newFixedThreadPool(levelManager.getServer()
                                                                             .getConfig()
                                                                             .getMaxChunkThreads());
        ((ThreadPoolExecutor) this.processorsThreadPool).setThreadFactory(runnable -> {
            Thread processingThread = new Thread(runnable);
            processingThread.setName("Chunk Processing Thread #" + this.processorThreadCount.incrementAndGet());
            return processingThread;
        });

        // Start up all chunk processors
        for (int i = 0; i < levelManager.getServer().getConfig().getMaxChunkThreads(); i++) {
            LevelChunkProcessor processor = new LevelChunkProcessor(this);
            this.processors.add(processor);
            this.processorsThreadPool.execute(processor);
        }
    }

    /**
     * Add a chunk request for the processors to handle.
     * @param request chunk request
     */
    public void addRequest(ChunkRequest request) {
        this.requests.add(request);
    }

    /**
     * Take a request from the queued chunk requests.
     * If none is available this will wait until one is available or the thread is interrupted.
     * @return chunk request
     * @throws InterruptedException if the thread is interrupted.
     */
    public ChunkRequest takeRequest() throws InterruptedException {
        return this.requests.take();
    }

    public ImplLevelManager getLevelManager() {
        return this.levelManager;
    }

    public void tick() {
        for (LevelChunkProcessor processor : this.processors) {
            processor.reset();  // reset the amount of requests it can take per tick
        }
    }

    public void close() {
        this.processorsThreadPool.shutdownNow();
    }
}
