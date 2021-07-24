package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class RequestChunkRadiusPacket extends ImplBedrockPacket {

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
