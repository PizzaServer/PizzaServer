package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class ChunkRadiusUpdatedPacket extends BedrockPacket {

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
