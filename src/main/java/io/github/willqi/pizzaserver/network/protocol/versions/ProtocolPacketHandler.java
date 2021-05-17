package io.github.willqi.pizzaserver.network.protocol.versions;

import io.github.willqi.pizzaserver.network.protocol.packets.BedrockPacket;
import io.netty.buffer.ByteBuf;

public interface ProtocolPacketHandler<P extends BedrockPacket> {

    P decode(ByteBuf buffer);
    void encode(P packet, ByteBuf buffer);

}
