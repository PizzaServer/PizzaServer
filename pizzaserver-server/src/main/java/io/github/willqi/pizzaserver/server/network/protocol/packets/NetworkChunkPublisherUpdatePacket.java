package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;

public class NetworkChunkPublisherUpdatePacket extends BedrockPacket {

    public static final int ID = 0x79;

    private Vector3i coordinates;
    private int radius;


    public NetworkChunkPublisherUpdatePacket() {
        super(ID);
    }

    public Vector3i getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Vector3i coordinates) {
        this.coordinates = coordinates;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
