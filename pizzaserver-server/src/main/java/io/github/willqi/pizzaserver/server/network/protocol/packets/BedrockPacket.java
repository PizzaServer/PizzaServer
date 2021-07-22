package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.APIBedrockPacket;

public abstract class BedrockPacket implements APIBedrockPacket {

    private final int packetId;

    public BedrockPacket(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketId() {
        return this.packetId;
    }

}
