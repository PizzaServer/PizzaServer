package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.mcworld.world.chunks.BedrockChunk;
import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelChunkPacket;
import io.github.willqi.pizzaserver.server.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

public class Chunk {

    private final BedrockChunk internalChunk;

    public Chunk(BedrockChunk chunk) {
        this.internalChunk = chunk;
    }

    /**
     * Serialize and send this chunk to a player
     * @param player
     */
    public void sendTo(Player player) {
        // Find the lowest from the top empty subchunk
        int subChunkCount = 15;
        for (; subChunkCount >= 0; subChunkCount--) {
            BedrockSubChunk subChunk = this.internalChunk.getSubChunk(subChunkCount);
            if (subChunk.getLayers().size() > 0) {
                break;
            }
        }
        subChunkCount++;

        // Write all subchunks
        ByteBuf packetData = ByteBufAllocator.DEFAULT.buffer();
        for (int subChunkIndex = 0; subChunkIndex < subChunkCount; subChunkIndex++) {
            BedrockSubChunk subChunk = this.internalChunk.getSubChunk(subChunkIndex);
            try {
                byte[] subChunkSerialized = subChunk.serializeForNetwork(player.getVersion());
                packetData.writeBytes(subChunkSerialized);
            } catch (IOException exception) {
                Server.getInstance().getLogger().error("Failed to serialize subchunk (x: " + this.internalChunk.getX() + " z: " + this.internalChunk.getZ() + " index: " + subChunkCount + ")");
                return;
            }
        }
        packetData.writeBytes(this.internalChunk.getBiomeData());
        packetData.writeByte(0);    // edu feature or smth

        byte[] data = new byte[packetData.readableBytes()];
        packetData.readBytes(data);
        packetData.release();

        // TODO: Supposedly tile entities are also packaged here
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.setX(this.internalChunk.getX());
        levelChunkPacket.setZ(this.internalChunk.getZ());
        levelChunkPacket.setSubChunkCount(subChunkCount);
        levelChunkPacket.setData(data);
        player.sendPacket(levelChunkPacket);
    }

}
