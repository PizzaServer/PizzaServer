package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BedrockPacket;

public abstract class ImplBedrockPacket implements BedrockPacket {

    private final int packetId;

    public ImplBedrockPacket(int packetId) {
        this.packetId = packetId;
    }

    @Override
    public int getPacketId() {
        return this.packetId;
    }

}
