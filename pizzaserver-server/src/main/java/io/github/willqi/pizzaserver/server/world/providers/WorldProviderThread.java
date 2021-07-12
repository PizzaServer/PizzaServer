package io.github.willqi.pizzaserver.server.world.providers;

import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.World;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.providers.actions.ChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.providers.actions.RequestChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.providers.actions.SendChunkToPlayerProcessingAction;

import java.io.Closeable;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for serializing chunks and queuing them to be sent to a player
 */
public class WorldProviderThread extends Thread implements Closeable {

    private final World world;
    private final WorldProvider provider;
    private final Queue<ChunkProcessingAction> actionsQueue = new ConcurrentLinkedQueue<>();


    public WorldProviderThread(World world, WorldProvider provider) {
        this.world = world;
        this.provider = provider;
    }

    public WorldProvider getProvider() {
        return this.provider;
    }

    public void addChunkToPlayerQueue(Player player, Chunk chunk) {
        synchronized (this) {
            this.actionsQueue.add(new SendChunkToPlayerProcessingAction(player, chunk));
            this.notify();
        }
    }

    public CompletableFuture<Chunk> requestChunk(int x, int z) {
        CompletableFuture<Chunk> response = new CompletableFuture<>();
        synchronized (this) {
            this.actionsQueue.add(new RequestChunkProcessingAction(this.world, x, z, response));
            this.notify();
        }
        return response;
    }


    // Thread methods

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (this.actionsQueue.peek() != null) {
                ChunkProcessingAction action = this.actionsQueue.poll();
                if (action instanceof RequestChunkProcessingAction) {
                    RequestChunkProcessingAction chunkRequestAction = (RequestChunkProcessingAction)action;
                    this.getProvider().onChunkRequest(chunkRequestAction);
                } else {
                    SendChunkToPlayerProcessingAction chunkSendAction = (SendChunkToPlayerProcessingAction)action;
                    chunkSendAction.getChunk().spawnTo(chunkSendAction.getPlayer());
                }
            }

            // Wait for whenever items are added to the queue instead of busy waiting
            synchronized (this) {
                if (this.actionsQueue.peek() == null) {
                    try {
                        this.wait();
                    } catch (InterruptedException exception) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.getProvider().close();
        this.interrupt();
    }
}
