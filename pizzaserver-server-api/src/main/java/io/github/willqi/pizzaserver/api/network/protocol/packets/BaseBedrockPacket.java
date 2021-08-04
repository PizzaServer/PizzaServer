package io.github.willqi.pizzaserver.api.network.protocol.packets;

public abstract class BaseBedrockPacket {

    private final int packetId;

    public BaseBedrockPacket(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketId() {
        return this.packetId;
    }

}
