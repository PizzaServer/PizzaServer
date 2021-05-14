package io.github.willqi.pizzaserver.network.protocol.versions;

import io.github.willqi.pizzaserver.network.protocol.packets.BedrockPacket;
import io.netty.buffer.ByteBuf;

public abstract class ProtocolPacketHandler<P extends BedrockPacket> {

    public abstract P decode(ByteBuf buffer);
    public abstract void encode(P packet, ByteBuf buffer);

}
