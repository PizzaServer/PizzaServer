package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.APIBedrockPacket;
import io.netty.buffer.ByteBuf;

public abstract class ProtocolPacketHandler<P extends APIBedrockPacket> {

    public P decode(ByteBuf buffer, PacketHelper helper) {
        throw new UnsupportedOperationException("This packet is not meant to be decoded server-side.");
    }

    public void encode(P packet, ByteBuf buffer, PacketHelper helper) {
        throw new UnsupportedOperationException("This packet is not meant to be sent to the client.");
    }

}
