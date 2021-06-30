package io.github.willqi.pizzaserver.server.world.chunks.processing;

import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.MCChunkDatabase;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.server.world.chunks.processing.actions.ChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.chunks.processing.actions.RequestChunkProcessingAction;
import io.github.willqi.pizzaserver.server.world.chunks.processing.actions.SendChunkToPlayerProcessingAction;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for serializing chunks and queuing them to be sent to a player
 */
public class ChunkThread extends Thread {

    private final ChunkManager chunkManager;
    private final Queue<ChunkProcessingAction> actionsQueue = new ConcurrentLinkedQueue<>();

    // Thread
    private MCChunkDatabase database;

    public ChunkThread(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
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
            this.actionsQueue.add(new RequestChunkProcessingAction(x, z, response));
            this.notify();
        }
        return response;
    }


    // Thread methods

    @Override
    public void run() {
        try (MCChunkDatabase database = this.chunkManager.getWorld().getInternalMCWorld().openChunkDatabase()) {
            this.database = database;

            // Poll for actions
            while (!Thread.currentThread().isInterrupted()) {
                while (this.actionsQueue.peek() != null) {
                    ChunkProcessingAction action = this.actionsQueue.poll();
                    if (action instanceof RequestChunkProcessingAction) {
                        RequestChunkProcessingAction chunkRequestAction = (RequestChunkProcessingAction)action;
                        this.handleChunkRequestAction(chunkRequestAction);
                    } else {
                        SendChunkToPlayerProcessingAction chunkSendAction = (SendChunkToPlayerProcessingAction)action;
                        this.handleSendChunkRequestAction(chunkSendAction);
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
        } catch (IOException exception) {
            Server.getInstance().getLogger().error("Failed chunk operation with .mcworld database for world named " + this.chunkManager.getWorld().getWorldFileName(), exception);
        }
    }

    private void handleChunkRequestAction(RequestChunkProcessingAction processingAction) {
        try {
            BedrockChunk internalChunk = this.database.getChunk(processingAction.getX(), processingAction.getZ());
            Chunk chunk = new Chunk(internalChunk);
            processingAction.getResponseFuture().complete(chunk);
        } catch (IOException exception) {
            processingAction.getResponseFuture().completeExceptionally(exception);
        }
    }

    private void handleSendChunkRequestAction(SendChunkToPlayerProcessingAction sendChunkAction) {
        sendChunkAction.getChunk().sendTo(sendChunkAction.getPlayer());
    }



}
