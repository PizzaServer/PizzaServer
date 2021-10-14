package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.api.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;

/**
 * Handles any packets regarding chunk/block interactions.
 */
public class ChunkBlockPacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public ChunkBlockPacketHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public void onPacket(RequestChunkRadiusPacket packet) {
        int newChunkRadius = Math.min(packet.getChunkRadiusRequested(), this.player.getServer().getConfig().getChunkRadius());
        this.player.setChunkRadius(newChunkRadius);

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
    }

}
