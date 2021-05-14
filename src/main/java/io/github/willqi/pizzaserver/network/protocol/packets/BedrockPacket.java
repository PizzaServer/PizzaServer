package io.github.willqi.pizzaserver.network.protocol.packets;

public abstract class BedrockPacket {

    private final int packetId;

    public BedrockPacket(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketId() {
        return this.packetId;
    }

}
