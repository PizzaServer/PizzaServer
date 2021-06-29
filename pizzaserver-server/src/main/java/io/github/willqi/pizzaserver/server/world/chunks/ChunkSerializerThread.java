package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.server.player.Player;

/**
 * Responsible for serializing chunks and queuing them to be sent to a player
 */
public class ChunkSerializerThread extends Thread {

    private final ChunkManager chunkManager;


    public ChunkSerializerThread(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public void run() {

    }

    public void addChunkToPlayerQueue(Player player, int x, int z) {

    }

    public void requestChunk(int x, int z) {

    }



}
