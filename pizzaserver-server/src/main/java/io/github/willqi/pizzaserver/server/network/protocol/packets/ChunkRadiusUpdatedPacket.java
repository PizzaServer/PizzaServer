package io.github.willqi.pizzaserver.server.network.protocol.packets;

/**
 * Sent in response to a RequestChunkRadiusPacket with the server's desired chunk radius for the client
 */
public class ChunkRadiusUpdatedPacket extends ImplBedrockPacket {

    public static final int ID = 0x46;

    private int radius;


    public ChunkRadiusUpdatedPacket() {
        super(ID);
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
}
