package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the server to render chunks in the players world.
 * a NetworkChunkPublisherUpdatePacket is REQUIRED in order for these chunks to show up
 */
public class WorldChunkPacket extends BaseBedrockPacket {

    public static final int ID = 0x3a;

    private int x;
    private int z;
    private int subChunkCount;
    private byte[] data = new byte[0];


    public WorldChunkPacket() {
        super(ID);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getSubChunkCount() {
        return this.subChunkCount;
    }

    public void setSubChunkCount(int subChunkCount) {
        this.subChunkCount = subChunkCount;
    }

    // See pizzaserver-worldfile package to see how bedrock chunks are serialized
    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }



}
