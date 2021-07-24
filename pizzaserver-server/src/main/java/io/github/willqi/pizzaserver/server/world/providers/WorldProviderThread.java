package io.github.willqi.pizzaserver.server.world.providers;

import io.github.willqi.pizzaserver.api.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.world.ImplWorld;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.server.world.providers.actions.ChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.providers.actions.ImplRequestChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.providers.actions.ImplSendChunkToPlayerProcessingAction;

import java.io.Closeable;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for serializing chunks and queuing them to be sent to a player
 */
public class WorldProviderThread extends Thread implements Closeable {

    private final ImplWorld world;
    private final BaseWorldProvider provider;
    private final Queue<ChunkProcessingAction> actionsQueue = new ConcurrentLinkedQueue<>();


    public WorldProviderThread(ImplWorld world, BaseWorldProvider provider) {
        this.world = world;
        this.provider = provider;
    }

    public BaseWorldProvider getProvider() {
        return this.provider;
    }

    public CompletableFuture<Void> requestSendChunkToPlayer(ImplPlayer player, ImplChunk chunk) {
        CompletableFuture<Void> response = new CompletableFuture<>();
        synchronized (this) {
            this.actionsQueue.add(new ImplSendChunkToPlayerProcessingAction(player, chunk, response));
            this.notify();
        }
        return response;
    }

    public CompletableFuture<Chunk> requestChunk(int x, int z) {
        CompletableFuture<Chunk> response = new CompletableFuture<>();
        synchronized (this) {
            this.actionsQueue.add(new ImplRequestChunkProcessingAction(this.world, x, z, response));
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
                if (action instanceof ImplRequestChunkProcessingAction) {
                    ImplRequestChunkProcessingAction chunkRequestAction = (ImplRequestChunkProcessingAction)action;
                    this.getProvider().onChunkRequest(chunkRequestAction);
                } else {
                    ImplSendChunkToPlayerProcessingAction chunkSendAction = (ImplSendChunkToPlayerProcessingAction)action;
                    chunkSendAction.getChunk().sendBlocksTo(chunkSendAction.getPlayer());
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
