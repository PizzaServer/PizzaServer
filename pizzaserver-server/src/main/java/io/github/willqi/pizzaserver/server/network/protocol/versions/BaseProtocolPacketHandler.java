package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.netty.buffer.ByteBuf;

public abstract class BaseProtocolPacketHandler<P extends BaseBedrockPacket> {

    public P decode(ByteBuf buffer, BasePacketHelper helper) {
        throw new UnsupportedOperationException("This packet is not meant to be decoded server-side.");
    }

    public void encode(P packet, ByteBuf buffer, BasePacketHelper helper) {
        throw new UnsupportedOperationException("This packet is not meant to be sent to the client.");
    }

}
