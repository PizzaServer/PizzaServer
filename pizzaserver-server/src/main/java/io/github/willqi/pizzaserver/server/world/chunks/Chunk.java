package io.github.willqi.pizzaserver.server.world.chunks;

import com.nukkitx.network.VarInts;
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

        ByteBuf packetData = ByteBufAllocator.DEFAULT.buffer();
        int breakingEmptySubChunkIndex = 0;
        for (; breakingEmptySubChunkIndex < 16; breakingEmptySubChunkIndex++) {
            BedrockSubChunk subChunk = this.internalChunk.getSubChunk(breakingEmptySubChunkIndex);
            if (subChunk == null) {
                break;
            }
            try {
                packetData.writeBytes(subChunk.serialize());
            } catch (IOException exception) {
                Server.getInstance().getLogger().error("Failed to serialize subchunk (x: " + this.internalChunk.getX() + " z: " + this.internalChunk.getZ() + " index: " + breakingEmptySubChunkIndex + ")");
                return;
            }
        }
        packetData.writeBytes(this.internalChunk.getBiomeData());
        packetData.writeByte(0);    // Edu purposes only
        VarInts.writeUnsignedInt(packetData, 0);    // No idea what this is

        byte[] data = new byte[packetData.readableBytes()];
        packetData.readBytes(data);
        packetData.release();

        // TODO: Supposedly tile entities are also packaged here
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.setX(this.internalChunk.getX());
        levelChunkPacket.setZ(this.internalChunk.getZ());
        levelChunkPacket.setSubChunkCount(breakingEmptySubChunkIndex);
        levelChunkPacket.setData(data);
        player.sendPacket(levelChunkPacket);
    }

}
