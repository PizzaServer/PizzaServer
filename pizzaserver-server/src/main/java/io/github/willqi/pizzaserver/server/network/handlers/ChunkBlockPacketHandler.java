package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
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
        this.player.setChunkRadiusRequested(packet.getChunkRadiusRequested());

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(this.player.getChunkRadius());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
    }

    @Override
    public void onPacket(PlayerActionPacket packet) {
        switch (packet.getActionType()) {
            case START_BREAK:
                if (this.player.canReach(packet.getVector3())) {
                    Block block = this.player.getWorld().getBlock(packet.getVector3());
                    if (block.getBlockType().isSolid()) {
                        this.player.setBlockBreaking(packet.getVector3());
                    }
                }
                break;
            case STOP_BREAK:
            case ABORT_BREAK:
                this.player.sendMessage(packet.getActionType().toString());
                this.player.setBlockBreaking(null);
                break;
        }
    }

}
