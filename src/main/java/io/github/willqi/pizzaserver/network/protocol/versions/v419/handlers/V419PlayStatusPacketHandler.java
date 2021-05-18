package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.network.protocol.packets.PlayStatusPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419PlayStatusPacketHandler implements ProtocolPacketHandler<PlayStatusPacket> {

    @Override
    public PlayStatusPacket decode(ByteBuf buffer) {
        return null;
    }

    @Override
    public void encode(PlayStatusPacket packet, ByteBuf buffer) {
        buffer.writeInt(packet.getStatus().ordinal());
    }

}
