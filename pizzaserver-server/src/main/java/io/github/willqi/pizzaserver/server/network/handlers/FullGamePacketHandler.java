package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.server.network.BedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ChunkRadiusUpdatedPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.RequestChunkRadiusPacket;
import io.github.willqi.pizzaserver.server.player.Player;

public class FullGamePacketHandler extends BedrockPacketHandler {

    private final Player player;


    public FullGamePacketHandler(Player player) {
        this.player = player;
    }

    @Override
    public void onPacket(RequestChunkRadiusPacket packet) {
        this.player.setChunkRadiusRequested(packet.getChunkRadiusRequested());

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
    }

}
