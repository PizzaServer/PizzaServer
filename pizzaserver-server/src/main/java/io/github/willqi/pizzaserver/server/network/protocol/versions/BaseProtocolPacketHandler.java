package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

public abstract class BaseProtocolPacketHandler<P extends BaseBedrockPacket> {

    public P decode(BasePacketBuffer buffer) {
        throw new UnsupportedOperationException("This packet is not meant to be decoded server-side.");
    }

    public void encode(P packet, BasePacketBuffer buffer) {
        throw new UnsupportedOperationException("This packet is not meant to be sent to the client.");
    }

}
