package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

/**
 * Sent by the client to the server with the requested chunk radius of the client.
 */
public class RequestChunkRadiusPacket extends BaseBedrockPacket {

    public final static int ID = 0x45;

    private int chunkRadiusRequested;


    public RequestChunkRadiusPacket() {
        super(ID);
    }

    public int getChunkRadiusRequested() {
        return this.chunkRadiusRequested;
    }

    public void setChunkRadiusRequested(int radiusRequested) {
        this.chunkRadiusRequested = radiusRequested;
    }

}
